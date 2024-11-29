package com.ouitrips.app.web.socialnetwork;

import com.ouitrips.app.constants.Response;
import com.ouitrips.app.services.socialnetwork.IPostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("${REST_NAME}/public/posts")
@AllArgsConstructor
public class PostController {
    private final IPostService postService;
    @PostMapping("/create")
    public ResponseEntity<?> createPost(
            @RequestParam(value = "description") String description,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "picture", required = false) String picture,
            @RequestParam(value = "style", required = false) String style,
            @RequestParam(value = "visibility", required = false) Boolean visibility,
//            @RequestParam(value = "profile_reference") Integer profileReference,
            @RequestParam(value = "circuit_reference") Integer circuitReference
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("description", description);
        params.put("content", content);
        params.put("picture", picture);
        params.put("style", style);
        params.put("visibility", visibility);
        params.put("circuitReference", circuitReference);
        return Response.responseData(postService.save(params));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePost(
            @RequestParam(value = "reference") Integer id,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "picture", required = false) String picture,
            @RequestParam(value = "style", required = false) String style,
            @RequestParam(value = "visibility", required = false) Boolean visibility,
//            @RequestParam(value = "profile_reference", required = false) Integer profileReference,
            @RequestParam(value = "circuit_reference", required = false) Integer circuitReference
    ) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("description", description);
        params.put("content", content);
        params.put("picture", picture);
        params.put("style", style);
        params.put("visibility", visibility);
        params.put("circuitReference", circuitReference);
        postService.save(params);
        return Response.updatedSuccessMessage();
    }
    @GetMapping("/get_by_follower")
    public ResponseEntity<?> getPostsByFollower() {
        return Response.responseData(postService.getByFollower());
    }
    @GetMapping("/get_by_profile")
    public ResponseEntity<?> getByProfile() {
        return Response.responseData(postService.getByProfile());
    }
    @GetMapping("/get_posts_by_profile")
    public ResponseEntity<?> getPostsByProfile(@RequestParam Integer profileId) {
        return Response.responseData(postService.getPostsByProfile(profileId));
    }
    @GetMapping("/get_all")
    public ResponseEntity<?> getAllPosts() {
        return Response.responseData(postService.getAll());
    }

    @GetMapping("/get")
    public ResponseEntity<?> getPost(@RequestParam Integer id) {
        return Response.responseData(postService.get(id));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePost(@RequestParam Integer id) {
        postService.delete(id);
        return Response.deletedSuccessMessage();
    }
}
