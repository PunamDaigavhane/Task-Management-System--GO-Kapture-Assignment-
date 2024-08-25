package com.example.test.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.test.entity.Task;
import com.example.test.repository.TaskRepository;

@Service
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	public Task createTask(Task task) {
		task.setCreatedAt(LocalDateTime.now());
		task.setUpdatedAt(LocalDateTime.now());
		return taskRepository.save(task);
	}

	public List<Task> getTasksByUser(Long userId) {
		return taskRepository.findByUserId(userId);
	}

	public Task updateTask(Task task) {
		task.setUpdatedAt(LocalDateTime.now());
		return taskRepository.save(task);
	}

	public void deleteTask(Long id) {
		taskRepository.deleteById(id);
	}

	public List<Task> filterTasks(Long userId, String status, String priority, LocalDate dueDate) {
		// Implement filtering logic based on parameters
		return taskRepository.findByUserIdAndStatus(userId, status);
	}

	public List<Task> searchTasks(Long userId, String query) {
		return taskRepository.findByUserIdAndTitleContainingOrDescriptionContaining(userId, query, query);
	}
}
