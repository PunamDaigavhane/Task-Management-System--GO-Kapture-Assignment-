package com.example.test.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.test.entity.Task;

@Repository

public interface TaskRepository extends JpaRepository<Task, Long> {
	List<Task> findByUserId(Long userId);

	List<Task> findByUserIdAndStatus(Long userId, String status);

	List<Task> findByUserIdAndPriority(Long userId, String priority);

	List<Task> findByUserIdAndDueDate(Long userId, LocalDate dueDate);

	List<Task> findByUserIdAndTitleContainingOrDescriptionContaining(Long userId, String title, String description);

}
