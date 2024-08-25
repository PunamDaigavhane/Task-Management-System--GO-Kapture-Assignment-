package com.example.test.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.test.entity.Task;
import com.example.test.entity.User;
import com.example.test.security.CustomUserDetails;
import com.example.test.service.TaskService;
import com.example.test.service.UserService;

@RestController
@RequestMapping("/tasks")

public class TaskController {

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public ResponseEntity<List<Task>> getTasks() {
		// Fetch user ID from the principal
		return ResponseEntity.ok(taskService.getTasksByUser(getUserIdFromPrincipal()));
	}

	@PostMapping("/create")
	public ResponseEntity<Task> createTask(@RequestBody Task task) {
		 Long userId = getUserIdFromPrincipal();
	        User user = new User();
	        user.setId(userId);
	        task.setUser(user);
		return ResponseEntity.ok(taskService.createTask(task));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
		// Fetch user ID and check if the task belongs to the user
		return ResponseEntity.ok(taskService.updateTask(task));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
		// Fetch user ID and check if the task belongs to the user
		taskService.deleteTask(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/filter")
	public ResponseEntity<List<Task>> filterTasks(@RequestParam(required = false) String status,
			@RequestParam(required = false) String priority,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate
			) {
		return ResponseEntity.ok(taskService.filterTasks(getUserIdFromPrincipal(), status, priority, dueDate));
	}

	@GetMapping("/search")
	public ResponseEntity<List<Task>> searchTasks(@RequestParam String query) {
		return ResponseEntity.ok(taskService.searchTasks(getUserIdFromPrincipal(), query));
	}

	private Long getUserIdFromPrincipal() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
	        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
	        return userDetails.getUserId();
	    }

	    throw new RuntimeException("User not found");
	}

}
