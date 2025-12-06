package com.backend.task.backend_task_app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.backend.task.backend_task_app.dto.UserRequest;
import com.backend.task.backend_task_app.model.Role;
import com.backend.task.backend_task_app.model.User;
import com.backend.task.backend_task_app.repository.UserRepository;
import com.backend.task.backend_task_app.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class UserController {

	private final UserService userService;
	private UserRepository userRepository;

	@Autowired
	public UserController(UserService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}
	
	 @GetMapping("/current")
	    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
	        // Get username from authentication
	        String username = authentication.getName();
	        
	        // Fetch user details from service
	        User user = userService.getUserByUsername(username);
	        
	        return ResponseEntity.ok(user);
	    }

	@GetMapping("/users")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasRole('ADMIN')")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}
	
	@GetMapping("/user")
	@ResponseStatus(HttpStatus.OK)
	public User getUserByUsername(@RequestParam String username) {
		return userService.getUserByUsername(username);
	}
	
	@GetMapping("/user/me")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<Map<String, Object>> getLoggedInUser() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    String username = auth.getName();
	    User user = userService.getUserByUsername(username);

	    if (user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	    }

	    Map<String, Object> userData = new HashMap<>();
	    userData.put("username", user.getUsername());
	    // Explicitly map roles to a list of role names to avoid serialization issues
	    userData.put("roles", user.getRoles().stream()
	            .map(Role::getRoleName)
	            .collect(Collectors.toList()));
	    
	    return ResponseEntity.ok(userData);
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserRequest userRequest) {
		
		if (userRequest.getPassword() == null || userRequest.getPassword().isBlank()) {
	        return ResponseEntity.badRequest().body("Password cannot be null or empty.");
	    }

		
		userService.registerUser(userRequest);
		return ResponseEntity.ok("User registered successfully. ");
	}
}

