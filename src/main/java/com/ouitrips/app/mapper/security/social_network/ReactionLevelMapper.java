package com.ouitrips.app.mapper.security.social_network;

import com.ouitrips.app.entities.social_network.ReactionLevel;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class ReactionLevelMapper implements Function<ReactionLevel, Object> {
    @Override
    public Object apply(ReactionLevel reactionLevel) {
        Map<String, Object> response = new HashMap<>();
        if (reactionLevel == null) {
            return response;
        }
        response.put("reference", reactionLevel.getId());
        response.put("value", reactionLevel.getValue());
        response.put("order", reactionLevel.getOrder());
        return response;
    }
}
