package com.ouitrips.app.mapper.security;

import com.ouitrips.app.utils.JSONUtils;
import com.ouitrips.app.entities.security.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.ouitrips.app.constants.StatusCodeConstant.STATUS_OK;

@Service
public class SignInMapper implements Function<User, Map<String, Object>> {
    @Override
    public Map<String, Object> apply(User user) {
        Map<String, Object> signInResponse = new HashMap<>();
        signInResponse.put("status", STATUS_OK);
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> dataUser = new HashMap<>();
        dataUser.put("reference", user.getReference());
        dataUser.put("email", user.getEmail());
        dataUser.put("avatar", user.getPicture()!=null?user.getPicture():"Default.png");
        dataUser.put("username", user.getUsername());
        dataUser.put("first_name", user.getFirstName() != null ? user.getFirstName() : "");
        dataUser.put("last_name", user.getLastName() != null ? user.getLastName() : "");
        dataUser.put("civility", user.getCivility()!=null?user.getCivility():"");
        dataUser.put("_token_", "");
        List<String> roleList = JSONUtils.jsonToList((String) user.getRoles());
        dataUser.put("roles", roleList);
        data.put("data_user", dataUser);
        HashMap<String, Object> info = new HashMap<>();
        info.put("active", true); // todo change this from db
        info.put("connected", true);
        info.put("lifetime", 50000);
        info.put("exist", true);
        info.put("error_password", false);
        info.put("locked", false);
        data.put("info", info);
        signInResponse.put("data", data);
        return signInResponse;
    }
}
