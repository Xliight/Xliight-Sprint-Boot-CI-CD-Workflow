package com.ouitrips.app.repositories.security.social_network;

import com.ouitrips.app.entities.social_network.Follower;
import com.ouitrips.app.entities.social_network.Post;
import com.ouitrips.app.entities.social_network.Profile;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {

    default List<Post> getByFollower(Profile profile, Sort sort) {
        Specification<Post> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Post, Profile> profileJoin = root.join("profile");
            Join<Profile, Follower> followerJoin = profileJoin.join("followers");
            predicates.add(criteriaBuilder.equal(followerJoin.get("follower"), profile));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
        return findAll(spec, sort);
    }

    List<Post> findByProfile(Profile profile, Sort sort);
}
