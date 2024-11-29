package com.ouitrips.app.mapper.security.social_network;

import com.ouitrips.app.entities.social_network.ReactionType;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class ReactionTypeMapper implements Function<ReactionType, Object> {
    @Override
    public Object apply(ReactionType reactionType) {
        Map<String, Object> response = new HashMap<>();
        if (reactionType == null) {
            return response;
        }
        response.put("reference", reactionType.getId());
        response.put("name", reactionType.getName());
        response.put("description", reactionType.getDescription());
        response.put("code", reactionType.getCode());
        response.put("status", reactionType.getStatus());
        return response;
    }
}
