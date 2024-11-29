package com.ouitrips.app.mapper.security.social_network;

import com.ouitrips.app.entities.social_network.Profile;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class ProfileMapper implements Function<Profile,Object> {
    @Override
    public Object apply(Profile profile) {
        Map<String, Object> response = new HashMap<>();
        if(profile == null)
            return response;
        response.put("reference", profile.getId());
        response.put("name", profile.getName());
        response.put("url_public", profile.getUrlPublic());
        return response;
    }
}
