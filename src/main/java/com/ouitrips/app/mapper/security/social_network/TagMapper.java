package com.ouitrips.app.mapper.security.social_network;

import com.ouitrips.app.entities.social_network.Tag;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class TagMapper implements Function<Tag, Object> {
    @Override
    public Object apply(Tag tag) {
        Map<String, Object> response = new HashMap<>();
        if (tag == null) {
            return response;
        }
        response.put("id", tag.getId());
        response.put("name", tag.getName());
        response.put("reference", tag.getReference());
        return response;
    }
}
