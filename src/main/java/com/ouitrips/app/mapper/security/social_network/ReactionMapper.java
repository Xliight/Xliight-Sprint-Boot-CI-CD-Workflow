package com.ouitrips.app.mapper.security.social_network;

import com.ouitrips.app.entities.social_network.Reaction;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class ReactionMapper implements Function<Reaction,Object> {
    private final ReactionTypeMapper reactionTypeMapper;
    private final ReactionLevelMapper reactionLevelMapper;
    private final ProfileMapper profileMapper;

    public ReactionMapper(ReactionTypeMapper reactionTypeMapper, ReactionLevelMapper reactionLevelMapper, ProfileMapper profileMapper) {
        this.reactionTypeMapper = reactionTypeMapper;
        this.reactionLevelMapper = reactionLevelMapper;
        this.profileMapper = profileMapper;
    }

    @Override
    public Object apply(Reaction reaction) {
        Map<String, Object> response = new HashMap<>();
        if (reaction == null) {
            return response;
        }
        response.put("reference", reaction.getId());
        response.put("date", reaction.getDate());
        response.put("profile", profileMapper.apply(reaction.getProfile()));
        response.put("reaction_type", reactionTypeMapper.apply(reaction.getReactionType()));
        response.put("reaction_level", reactionLevelMapper.apply(reaction.getReactionLevel()));
        return response;
    }
}
