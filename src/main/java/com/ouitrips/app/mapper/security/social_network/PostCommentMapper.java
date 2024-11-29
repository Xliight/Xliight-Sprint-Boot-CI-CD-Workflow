package com.ouitrips.app.mapper.security.social_network;

import com.ouitrips.app.entities.social_network.PostComment;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Configuration
public class PostCommentMapper implements Function<PostComment, Object> {
    @Override
    public Object apply(PostComment postComment) {
        Map<String, Object> response = new HashMap<>();
        if (postComment == null) {
            return response;
        }
        response.put("reference", postComment.getId());
        response.put("content", postComment.getContent());
        response.put("reaction_number", postComment.getReactionNumber());
        //Todo add profile info
        return response;
    }
}
