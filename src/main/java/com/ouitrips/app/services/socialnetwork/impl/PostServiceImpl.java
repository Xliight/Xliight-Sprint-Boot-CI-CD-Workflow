package com.ouitrips.app.services.socialnetwork.impl;

import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.entities.social_network.*;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.CircuitMapper;
import com.ouitrips.app.mapper.security.social_network.PostMapper;
import com.ouitrips.app.repositories.security.circuits.CircuitRepository;
import com.ouitrips.app.repositories.security.social_network.*;
import com.ouitrips.app.services.socialnetwork.IPostService;
import com.ouitrips.app.utils.UserUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class PostServiceImpl implements IPostService {
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final CircuitRepository circuitRepository;
    private final PostMapper postMapper;
    private final UserUtils userUtils;
    private final CircuitMapper circuitMapper;

    @Override
    public Object save(Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        String description = (String) params.get("description");
        String content = (String) params.get("content");
        String picture = (String) params.get("picture");
        String style = (String) params.get("style");
        Boolean visibility = (Boolean) params.get("visibility");
        Integer circuitReference = (Integer) params.get("circuit_reference");
        Post post;
        if (id == null) {
            post = new Post();
            post.setPostDate(Instant.now());
        } else {
           post = this.getById(id);
        }
        if (description != null) {
            post.setDescription(description);
        }
        if (content != null) {
            post.setContent(content);
        }
        if (picture != null) {
            post.setPicture(picture);
        }
        if (style != null) {
            post.setStyle(style);
        }
        if (visibility != null) {
            post.setVisibility(visibility);
        }
        User userConnected = userUtils.userAuthenticated();
        Profile profile = profileRepository.findByUser(userConnected);
        if (profile == null){
            throw new ExceptionControllerAdvice.ObjectNotFoundException("Profile not found");
        }

        post.setProfile(profile);
        if (circuitReference != null) {
            Circuit circuit = circuitRepository.findById(circuitReference)
                    .orElseThrow(() -> new ExceptionControllerAdvice.GeneralException("Circuit with ID " + circuitReference + " not found"));
            post.setCircuit(circuit);
        }
        return Map.of("reference", postRepository.save(post).getId());
    }
    @Override
    public void delete(Integer id) {
        postRepository.delete(this.getById(id));
    }
    @Override
    public List<Object> getByFollower() {
        User userConnected = userUtils.userAuthenticated();
        Profile profile = profileRepository.findByUser(userConnected);
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        return postRepository.getByFollower(profile, sortById)
                .stream().
                map(postMapper)
                .toList();
    }
    @Override
    public List<Object> getByProfile() {
        User userConnected = userUtils.userAuthenticated();
        Profile profile = profileRepository.findByUser(userConnected);
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");

        return postRepository.findByProfile(profile, sortById)
                .stream()
                .map(postMapper)
                .toList();
    }
    @Override
    @Transactional
    public Object get(Integer id) {
        Post post = this.getById(id);
        Map<String, Object> postResponse = (Map<String, Object>) postMapper.apply(post);
        Circuit circuit = post.getCircuit();
        if (circuit != null) {
            Map<String, Object> detailedCircuit = (Map<String, Object>) circuitMapper.applyDetail(circuit);
            postResponse.put("circuit", detailedCircuit);
        }

        return postResponse;
    }
    @Override
    @Transactional
    public List<Object> getPostsByProfile(Integer profileId) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ExceptionControllerAdvice.ObjectNotFoundException("Profile not found"));
        Sort sortById = Sort.by(Sort.Direction.DESC, "id");
        return postRepository.findByProfile(profile, sortById)
                .stream()
                .map(postMapper)
                .toList();


    }
    @Override
    @Transactional
    public List<Object> getAll() {
        return postRepository.findAll().stream().map(postMapper).toList();

    }
    public Post getById(Integer id) {
        return postRepository.findById(id).orElseThrow(
                () -> new ExceptionControllerAdvice.ObjectNotFoundException("Post not found")
        );
    }
}
