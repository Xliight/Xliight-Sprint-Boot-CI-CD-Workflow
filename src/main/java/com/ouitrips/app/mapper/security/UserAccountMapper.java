package com.ouitrips.app.mapper.security;
import com.ouitrips.app.utils.JSONUtils;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.utils.DateUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class UserAccountMapper implements Function<User, Object> {
    private final DateUtil dateUtil;
    @Override
    public Object apply(User user) {
        Map<String, Object> response = new HashMap<>();
        response.put("first_name", user.getFirstName() != null ? user.getFirstName() : "");
        response.put("last_name", user.getLastName() != null ? user.getLastName() : "");
        response.put("username", user.getUsername());
        response.put("birth_date", user.getBirthDate() != null ? dateUtil.parseString(user.getBirthDate(), "YYYY-MM-dd") : "");
        response.put("created_date", user.getCreatedDate() != null ? dateUtil.parseString(user.getCreatedDate(), "YYYY-MM-dd HH:mm:ss") : "");
        response.put("birth_place", user.getBirthPlace() != null ? user.getBirthPlace() : "");
        response.put("email", user.getEmail() != null ? user.getEmail() : "");
        response.put("city", user.getCity() != null ? user.getCity() : "");
        response.put("phone", user.getPhone() != null ? user.getPhone() : "");
        response.put("country", user.getCountry() != null ? user.getCountry() : "");
        response.put("civility", user.getCivility() != null ? user.getCivility() : "");
        response.put("zip_code", user.getZipCode() != null ? user.getZipCode() : "");
        response.put("address", user.getAddress() != null ? user.getAddress() : "");
        response.put("complement_address", user.getAdditionalAddress() != null ? user.getAdditionalAddress() : "");
        response.put("picture", user.getPicture() != null ? user.getPicture() : "");
        response.put("enabled", user.getEnabled());
        response.put("prefix", user.getPrefixe() != null ? user.getPrefixe() : "");
        response.put("google_id", user.getGoogleId() != null ? user.getGoogleId() : "");
        response.put("facebook_id", user.getFacebookId() != null ? user.getFacebookId() : "");
        response.put("gsm", user.getGsm() != null ? user.getGsm() : "");
        response.put("iso2", user.getIso2() != null ? user.getIso2() : "");
        response.put("mobile_phone", user.getMobilePhone() != null ? user.getMobilePhone() : "");
        response.put("profession", user.getProfession() != null ? user.getProfession() : "");
        response.put("reference", user.getReference() != null ? user.getReference() : "");
        List<String> roleList = JSONUtils.jsonToList((String)user.getRoles());
        response.put("roles", roleList);
        return response;
    }
}