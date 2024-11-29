package com.ouitrips.app.repositories.security;

import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.enums.Role;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
    User findByUsername(String username);
    List<User> findByRolesIn(List<Role> roleNames);
    User findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByReference(String reference);
    Boolean existsByEmail(String email);
    Boolean existsByMobilePhone(String mobilePhone);
    Boolean existsByGsm(String gsm);
    List<User> findByRoles(Role role);
    Optional<User> findByReference(String reference);
    List<User> findUsersByEnabledTrue();
    List<User> findUsersByEnabledFalse();
    List<User> findUsersByEnabled(Boolean status);
    Optional<User> findUserByMobilePhone(String mobilePhone);
    Optional<User> findFirstByMobilePhone(String mobilePhone); //only for test
    Optional<User> findUserByPhone(String phone);
    Optional<User> findUserByGsm(String gsm);

    default List<User> findUserByGsmPrefixe(String contact) {//find by prefix+gsm
        Specification<User> spec = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            Expression<String> concatenated = criteriaBuilder.concat(root.get("prefixe"), root.get("gsm"));
            predicates.add(criteriaBuilder.equal(concatenated, contact));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return findAll(spec);
    }

    default List<User> getAllUsers(){
        Specification<User> spec = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();
            query.orderBy(criteriaBuilder.asc(root.get("username")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return this.findAll(spec);
    }

    default List<User> getUserByRoles(String role){
        Specification<User> spec = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(role!=null) predicates.add(
                    criteriaBuilder.like(root.get("roles"), "%"+role+"%")
            );

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return this.findAll(spec);
    }

    default Optional<User> getUserByEmailOrUsername(String email, String username){
        Specification<User> spec = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(email!=null)
                predicates.add(
                        criteriaBuilder.equal(root.get("email"), email)
                );
            if(username!=null)
                predicates.add(
                        criteriaBuilder.equal(root.get("username"), username)
                );
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
        return this.findOne(spec);
    }

    default Long countNewAccounts(Date dateNow,
                                  Integer daysInterval,
                                  String periodicite){
        Specification<User> spec = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            Instant startDate;
            if(dateNow!=null && daysInterval!=null && periodicite!=null){
                calendar.setTime(dateNow);
                switch (periodicite) {
                    case "D" -> calendar.add(Calendar.DAY_OF_MONTH, -daysInterval);
                    case "M" -> calendar.add(Calendar.MONTH, -daysInterval);
                    case "Y" -> calendar.add(Calendar.YEAR, -daysInterval);
                    case "H" -> calendar.add(Calendar.HOUR_OF_DAY, -daysInterval);
                    case "MIN" -> calendar.add(Calendar.MINUTE, -daysInterval);
                    default ->
                            throw new ExceptionControllerAdvice.GeneralException("Unsupported periodicite: " + periodicite);
                }
            }
            startDate = calendar.toInstant();
            if(startDate!=null && dateNow!=null)
                predicates.add(criteriaBuilder.between(root.get("createdDate"), startDate, dateNow.toInstant()));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
        return this.count(spec);
    }


    default List<User> lastUsers(Integer limit, Integer offset){
        if (limit == null) {
            limit = 10; // Default limit
        }
        if (offset == null) {
            offset = 0; // Default offset
        }
        Specification<User> spec = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            query.orderBy(criteriaBuilder.desc(root.get("username")));

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
        Pageable pageable = PageRequest.of(offset, limit);
        return this.findAll(spec, pageable).getContent();
    }

    default Optional<User> getUserByEmailOrTel(String email, String phone){
        Specification<User> spec = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(email!=null)
                predicates.add(
                        criteriaBuilder.equal(root.get("email"), email)
                );
            if(phone!=null)
                predicates.add(
                        criteriaBuilder.equal(root.get("mobilePhone"), phone)
                );
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
        return this.findOne(spec);
    }

    default Optional<User> getUserByTokenPassword(String tokenPassword){
        Specification<User> spec = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(tokenPassword!=null)
                predicates.add(
                        criteriaBuilder.equal(root.get("tokenPassword"), tokenPassword)
                );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return this.findOne(spec);
    }


    default Optional<User> getUserBySocialId(String socialId){//todo : need to test
        Specification<User> spec = (root, query, criteriaBuilder) ->{
            List<Predicate> predicates = new ArrayList<>();

            if(socialId!=null)
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.equal(root.get("googleId"), socialId),
                                criteriaBuilder.equal(root.get("facebookId"), socialId)
                        )
                );
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return this.findOne(spec);
    }
}
