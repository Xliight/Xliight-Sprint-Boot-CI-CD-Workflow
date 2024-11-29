package com.ouitrips.app.services.socialnetwork.impl;

import com.ouitrips.app.entities.circuits.Circuit;
import com.ouitrips.app.entities.security.User;
import com.ouitrips.app.entities.social_network.Post;
import com.ouitrips.app.entities.social_network.Profile;
import com.ouitrips.app.exceptions.ExceptionControllerAdvice;
import com.ouitrips.app.mapper.security.circuits.CircuitMapper;
import com.ouitrips.app.mapper.security.social_network.PostMapper;
import com.ouitrips.app.repositories.security.circuits.CircuitRepository;
import com.ouitrips.app.repositories.security.social_network.PostRepository;
import com.ouitrips.app.repositories.security.social_network.ProfileRepository;
import com.ouitrips.app.utils.UserUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private CircuitRepository circuitRepository;
    @Mock
    private PostMapper postMapper;
    @Mock
    private CircuitMapper circuitMapper;
    @Mock
    private UserUtils userUtils;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
     void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_withCircuit() {
        User user = new User();
        user.setUsername("username");
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setId(1);
        Post post = new Post();
        post.setId(1);
        Circuit circuit = new Circuit();
        circuit.setId(1);

        // Mocking the user authentication and profile retrieval
        when(userUtils.userAuthenticated()).thenReturn(user);
        when(profileRepository.findByUser(user)).thenReturn(profile);
        // Arrange
        Integer circuitReference = 1;
        Map<String, Object> params = new HashMap<>();
        params.put("description", "New Post Description");
        params.put("content", "Post Content");
        params.put("visibility", true);
        params.put("style","dsfd");
        params.put("picture", "New Post picture");
        params.put("circuit_reference", circuitReference);


        // Mocking the circuitRepository to return a Circuit when a valid ID is passed
        when(circuitRepository.findById(circuitReference)).thenReturn(Optional.of(circuit));
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Map<String, Object> result = (Map<String, Object>) postService.save(params);
        assertNotNull(result);
        assertEquals(1,result.get("reference"));
    }

    @Test
     void testSave_NewPost_Success() {

        Map<String, Object> params = new HashMap<>();
        params.put("description", "New Post Description");
        params.put("content", "Post Content");
        params.put("visibility", true);
        params.put("style","dsfd");
        params.put("picture", "New Post picture");


        User user = new User();
        user.setUsername("username");
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setId(1);
        Post post = new Post();
        post.setId(1);

        when(userUtils.userAuthenticated()).thenReturn(user);
        when(profileRepository.findByUser(user)).thenReturn(profile);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        Map<String, Object> result = (Map<String, Object>) postService.save(params);
        assertNotNull(result);
        assertEquals(1,result.get("reference"));
    }


    @Test
     void testSave_UpdatePost_Success() {
        Map<String, Object> params = new HashMap<>();
        params.put("id", 1);
        params.put("description", "Updated Description");
        User user = new User();
        user.setUsername("username");
        Post existingPost = new Post();
        existingPost.setId(1);
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setId(1);
        when(userUtils.userAuthenticated()).thenReturn(user);
        when(profileRepository.findByUser(user)).thenReturn(profile);
        when(postRepository.findById(1)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenReturn(existingPost);

        Map<String, Object> result = (Map<String, Object>) postService.save(params);
        assertNotNull(result);
        assertEquals(1,result.get("reference"));
    }

    @Test
     void testSave_ProfileNotFound_ThrowsException() {
        Map<String, Object> params = new HashMap<>();
        when(userUtils.userAuthenticated()).thenReturn(new User());
        when(profileRepository.findByUser(any(User.class))).thenReturn(null);

        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () ->  postService.save(params)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Profile not found", exception.getContent());
    }

    @Test
     void testSave_CircuitNotFound_ThrowsException() {
        Map<String, Object> params = new HashMap<>();
        params.put("circuit_reference", 99);

        when(userUtils.userAuthenticated()).thenReturn(new User());
        when(profileRepository.findByUser(any(User.class))).thenReturn(new Profile());
        when(circuitRepository.findById(99)).thenReturn(Optional.empty());

        ExceptionControllerAdvice.GeneralException exception = assertThrows(
                ExceptionControllerAdvice.GeneralException.class,
                () ->  postService.save(params)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Circuit with ID 99 not found", exception.getContent());
    }

    @Test
     void testDelete_PostExists_Success() {
        Post post = new Post();
        post.setId(1);

        when(postRepository.findById(1)).thenReturn(Optional.of(post));

        postService.delete(1);

        verify(postRepository).delete(post);
    }

    @Test
     void testDelete_PostNotFound_ThrowsException() {
        when(postRepository.findById(1)).thenReturn(Optional.empty());

        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () ->  postService.delete(1)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Post not found", exception.getContent());

    }

    @Test
    void testGetByFollower() {
        // Arrange
        User mockedUser = mock(User.class);
        Profile mockedProfile = mock(Profile.class);
        Post mockedPost = mock(Post.class);
        Object mappedPost = mock(Object.class); // The mapped result (since you are using Object as return type)

        // Mock the behavior of the dependencies
        when(userUtils.userAuthenticated()).thenReturn(mockedUser);
        when(profileRepository.findByUser(mockedUser)).thenReturn(mockedProfile);
        when(postRepository.getByFollower(mockedProfile, Sort.by(Sort.Direction.DESC, "id")))
                .thenReturn(List.of(mockedPost));  // Return a list with one mocked post
        when(postMapper.apply(mockedPost)).thenReturn(mappedPost);  // Mock postMapper to return the mapped object

        // Act
        List<Object> result = postService.getByFollower();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());  // Should return a list of size 1
        assertEquals(mappedPost, result.get(0));  // The result should contain the mapped post
        verify(userUtils).userAuthenticated();  // Ensure userUtils was called
        verify(profileRepository).findByUser(mockedUser);  // Ensure profileRepository was called
        verify(postRepository).getByFollower(mockedProfile, Sort.by(Sort.Direction.DESC, "id"));  // Verify postRepository method call
        verify(postMapper).apply(mockedPost);  // Ensure postMapper was called
    }

    @Test
    void testGetByProfile() {
        // Arrange
        User mockedUser = mock(User.class);
        Profile mockedProfile = mock(Profile.class);
        Post mockedPost = mock(Post.class);
        Object mappedPost = mock(Object.class); // The mapped result (since you're using Object as return type)

        // Mock the behavior of the dependencies
        when(userUtils.userAuthenticated()).thenReturn(mockedUser);
        when(profileRepository.findByUser(mockedUser)).thenReturn(mockedProfile);
        when(postRepository.findByProfile(mockedProfile, Sort.by(Sort.Direction.DESC, "id")))
                .thenReturn(List.of(mockedPost));  // Return a list with one mocked post
        when(postMapper.apply(mockedPost)).thenReturn(mappedPost);  // Mock postMapper to return the mapped object

        // Act
        List<Object> result = postService.getByProfile();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());  // Should return a list of size 1
        assertEquals(mappedPost, result.get(0));  // The result should contain the mapped post
        verify(userUtils).userAuthenticated();  // Ensure userUtils was called
        verify(profileRepository).findByUser(mockedUser);  // Ensure profileRepository was called
        verify(postRepository).findByProfile(mockedProfile, Sort.by(Sort.Direction.DESC, "id"));  // Verify postRepository method call
        verify(postMapper).apply(mockedPost);  // Ensure postMapper was called
    }


    @Test
    void testGet_withCircuit() {
        // Arrange
        Integer postId = 1;  // ID for the post to retrieve
        Post mockedPost = mock(Post.class);
        mockedPost.setId(postId);
        Circuit mockedCircuit = mock(Circuit.class);
        mockedCircuit.setId(postId);
        Map<String, Object> mappedPost = new HashMap<>();
        mappedPost.put("reference", 1);
        Map<String, Object> detailedCircuit = new HashMap<>();
        detailedCircuit.put("circuit", mockedCircuit);

        // Mock the behavior of the dependencies
        when(postRepository.findById(postId)).thenReturn(java.util.Optional.of(mockedPost));
        when(postMapper.apply(mockedPost)).thenReturn(mappedPost);
        when(mockedPost.getCircuit()).thenReturn(mockedCircuit);
        when(circuitMapper.applyDetail(mockedCircuit)).thenReturn(detailedCircuit);
        // Act
        Object result = postService.get(postId);
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Map);  // Ensure the result is a Map
        Map<String, Object> resultMap = (Map<String, Object>) result;// Ensure the circuit details are added if present
        assertEquals(1, resultMap.get("reference"));
        // Verify that the appropriate methods were called
        verify(postMapper).apply(mockedPost);
        verify(mockedPost).getCircuit();
        verify(circuitMapper).applyDetail(mockedCircuit);
    }

    @Test
    void testGet_withoutCircuit() {
        // Arrange
        Integer postId = 1;  // ID for the post to retrieve
        Post mockedPost = mock(Post.class);
        mockedPost.setId(postId);
        Map<String, Object> mappedPost = new HashMap<>();

        // Mock the behavior of the dependencies
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockedPost));  // Return an Optional with the mocked post
        when(postMapper.apply(mockedPost)).thenReturn(mappedPost);  // Mock postMapper to return a map
        when(mockedPost.getCircuit()).thenReturn(null);  // No circuit associated
        // Act
        Object result = postService.get(postId);
        // Assert
        assertNotNull(result, "The result should not be null");
        assertTrue(result instanceof Map, "The result should be a Map");  // Ensure the result is a Map
        Map<String, Object> resultMap = (Map<String, Object>) result;
        assertNull(resultMap.get("circuit"), "There should be no circuit data if the post does not have a circuit");

        // Verify that the appropriate methods were called
        verify(postMapper).apply(mockedPost);
        verify(mockedPost).getCircuit();
        verifyNoInteractions(circuitMapper);  // Ensure circuitMapper was not called when no circuit is present
    }


    @Test
    void testGetPostsByProfile() {
        Integer profileId = 1;
        Profile mockedProfile = mock(Profile.class);
        Post mockedPost = mock(Post.class);
        Map<String, Object> mappedPost = mock(Map.class);
        List<Post> posts = List.of(mockedPost);

        // Mock the behavior of the repositories
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(mockedProfile));
        when(postRepository.findByProfile(mockedProfile, Sort.by(Sort.Direction.DESC, "id"))).thenReturn(posts);
        when(postMapper.apply(mockedPost)).thenReturn(mappedPost);

        // Act
        List<Object> result = postService.getPostsByProfile(profileId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());  // Verify that one post is returned
        assertEquals(mappedPost, result.get(0));  // Verify the post mapping

        // Verify that methods are called
        verify(profileRepository).findById(profileId);
        verify(postRepository).findByProfile(mockedProfile, Sort.by(Sort.Direction.DESC, "id"));
        verify(postMapper).apply(mockedPost);
    }

    @Test
    void testGetPostsByProfile_profileNotFound() {
        Integer profileId = 999;  // ID that does not exist
        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());  // Return an empty Optional


        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () ->  postService.getPostsByProfile(profileId)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Profile not found", exception.getContent());
    }


    @Test
    void testGetAll() {
        // Prepare mock data
        Post mockedPost1 = mock(Post.class);
        Post mockedPost2 = mock(Post.class);
        Map<String, Object> mappedPost1 = mock(Map.class);
        Map<String, Object> mappedPost2 = mock(Map.class);

        List<Post> posts = List.of(mockedPost1, mockedPost2);

        // Mock the repository to return the list of posts
        when(postRepository.findAll()).thenReturn(posts);
        when(postMapper.apply(mockedPost1)).thenReturn(mappedPost1);
        when(postMapper.apply(mockedPost2)).thenReturn(mappedPost2);

        // Act
        List<Object> result = postService.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mappedPost1, result.get(0));
        assertEquals(mappedPost2, result.get(1));

        // Verify that the repository method is called
        verify(postRepository).findAll();
        verify(postMapper).apply(mockedPost1);
        verify(postMapper).apply(mockedPost2);
    }


    @Test
    void testGetById_Success() {
        // Arrange
        Post mockedPost = mock(Post.class);

        Integer postId = 1;
        when(postRepository.findById(postId)).thenReturn(Optional.of(mockedPost));

        // Act
        Post result = postService.getById(postId);

        // Assert
        assertNotNull(result); // The result should not be null
        assertEquals(mockedPost, result); // The result should be the mocked post

        // Verify that the repository method is called
        verify(postRepository).findById(postId);
    }

    @Test
    void testGetById_NotFound() {
        // Arrange
        Integer postId = 1;
        when(postRepository.findById(postId)).thenReturn(Optional.empty()); // Simulate not found


        ExceptionControllerAdvice.ObjectNotFoundException exception = assertThrows(
                ExceptionControllerAdvice.ObjectNotFoundException.class,
                () ->  postService.getById(postId)
        );
        Assertions.assertNotNull(exception);
        Assertions.assertEquals("Post not found", exception.getContent());

        // Verify that the repository method is called
        verify(postRepository).findById(postId);
    }
}