package com.nelson.greg.restful.user.resource;

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

import com.nelson.greg.restful.exception.UserNotFoundException;
import com.nelson.greg.restful.repository.UserRepository;
import com.nelson.greg.restful.user.entity.User;


@RestController
public class UserResource {

	@Autowired
	private UserRepository userRepository;

	@GetMapping(path = "/users")
	public List<User> retrieveAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping(path = "/users/{id}")
	public Optional<User> retrieveUser(@PathVariable BigInteger id) {
		Optional<User> user = userRepository.findById(id);
		if (!user.isPresent()) {
			throw new UserNotFoundException("User ID :" + id + " NOT FOUND!");
		} else
			return user;
	}

	@PostMapping("/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = userRepository.save(user);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId())
				.toUri();

		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("/users/{id}")
	public void deleteById(@PathVariable BigInteger id) {
		userRepository.deleteById(id);
	}

}