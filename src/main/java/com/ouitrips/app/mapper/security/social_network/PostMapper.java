package com.ouitrips.app.mapper.security.social_network;

import com.ouitrips.app.entities.social_network.*;
import com.ouitrips.app.mapper.security.circuits.CircuitMapper;
import com.ouitrips.app.repositories.security.social_network.LinksPtgRepository;
import org.springframework.context.annotation.Configuration;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class PostMapper implements Function<Post,Object> {
    private final PostCommentMapper postCommentMapper;
    private final ReactionMapper reactionMapper;
    private final CircuitMapper circuitMapper;
    private final ProfileMapper profileMapper;
    private final LinksPtgRepository linksPtgRepository;
    private final TagMapper tagMapper;

    public PostMapper(PostCommentMapper postCommentMapper, ReactionMapper reactionMapper, CircuitMapper circuitMapper, ProfileMapper profileMapper, LinksPtgRepository linksPtgRepository, TagMapper tagMapper) {
        this.postCommentMapper = postCommentMapper;
        this.reactionMapper = reactionMapper;
        this.circuitMapper = circuitMapper;
        this.profileMapper = profileMapper;
        this.linksPtgRepository = linksPtgRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public Object apply(Post post) {

        Map<String, Object> response = new HashMap<>();
        if(post == null)
            return response;
        response.put("reference", post.getId());
        response.put("description", post.getDescription());
        response.put("content", post.getContent());
        response.put("picture", post.getPicture());
        response.put("style", post.getStyle());
        response.put("visibility", post.isVisibility());
        response.put("circuit", circuitMapper.apply(post.getCircuit()));
        response.put("profile", profileMapper.apply(post.getProfile()));
        // PostComment mapping
        Set<PostComment> postComments = post.getComments();
        if(postComments != null){
            Set<Object> postCommentsResponse = post.getComments().stream()
                    .map(postCommentMapper)
                    .collect(Collectors.toSet());
            response.put("post_comment", postCommentsResponse);
        }
        // Reaction mapping
        Set<Reaction> reactions = post.getReactions();
        if (reactions != null && !reactions.isEmpty()) {
            List<Object> reactionsResponse = post.getReactions().stream()
                    .map(reactionMapper)
                    .toList();
            response.put("reactions", reactionsResponse);
        }
        // Tag mapping
        List<LinksPtg> linksPtgList = linksPtgRepository.findByPost(post);
        List<Tag> tags = new ArrayList<>();
        for (LinksPtg link : linksPtgList) {
            tags.add(link.getTag());
        }
        List<Object> tagsResponse = tags.stream()
                .map(tagMapper)
                .toList();
        response.put("tags", tagsResponse);

        return response;
    }
}
