package com.nelson.greg.restful.resource;

import java.math.BigInteger;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelson.greg.restful.entity.Post;
import com.nelson.greg.restful.entity.User;
import com.nelson.greg.restful.exception.UserNotFoundException;
import com.nelson.greg.restful.repository.PostRepository;
import com.nelson.greg.restful.repository.UserRepository;

@RestController
public class UserResource {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@GetMapping(path = "/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping(path = "/users/{id}")
	public Optional<User> retrieveUser(@PathVariable BigInteger id) {
		Optional<User> userOpt = userRepository.findById(id);

		if (!userOpt.isPresent()) {
			throw new UserNotFoundException("User ID :" + id + " NOT FOUND!");
		} else
			return userOpt;
	}

	@GetMapping(path = "/users/{id}/posts")
	public List<Post> retrieveUserPosts(@PathVariable BigInteger id) {
		Optional<User> userOpt = userRepository.findById(id);

		if (!userOpt.isPresent()) {
			throw new UserNotFoundException("User ID :" + id + " NOT FOUND!");
		} else
			return userOpt.get().getPosts();
	}

	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User user2Save = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user2Save.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@PostMapping("/users/{id}/posts")
	public ResponseEntity<Object> createPost(@Valid @PathVariable BigInteger id, @RequestBody Post post) {

		Optional<User> userOpt = userRepository.findById(id);

		if (!userOpt.isPresent()) {
			throw new UserNotFoundException("User ID :" + id + " NOT FOUND!");
		}

		User user = userOpt.get();

		post.setUser(user);

		postRepository.save(post);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/users/{id}")
	public void deleteUserById(@PathVariable BigInteger id) {
		userRepository.deleteById(id);
	}
	
	@DeleteMapping("/users/{userId}/posts/{postId}")
	public void deletePostById(@PathVariable BigInteger userId, @PathVariable BigInteger postId) {
		Optional<User> userOpt = userRepository.findById(userId);

		if (!userOpt.isPresent()) {
			throw new UserNotFoundException("User ID :" + userId + " NOT FOUND!");
		}
		
		Optional<Post> postOpt = postRepository.findById(postId);

		if (!postOpt.isPresent()) {
			throw new UserNotFoundException("User ID :" + userId + "Post ID : " + postId + " NOT FOUND!");
		} else postRepository.deleteById(postId);
	}
}