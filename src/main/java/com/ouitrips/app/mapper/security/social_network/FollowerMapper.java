package com.ouitrips.app.mapper.security.social_network;

import com.ouitrips.app.entities.social_network.Follower;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class FollowerMapper implements Function<Follower,Object> {
    @Override
    public Object apply(Follower follower) {
        Map<String, Object> response = new HashMap<>();
        if(follower == null)
            return response;
        response.put("reference", follower.getId());
        response.put("dateFollow", follower.getDateFollow());
        response.put("dateUnfollow", follower.getDateUnfollow());
        response.put("isApproved", follower.isApproved());
        return response;
    }
}
