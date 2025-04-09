package com.backend.task.backend_task_app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String taskName;
	private int duration;
	//Assigned user
	private String assignedTo;

	// Track if task is marked completed by the user
	private boolean isCompleted = false;

	// Track if the admin has confirmed the completion
	private boolean isConfirmed = false;

	@ManyToOne
	@JoinColumn(name = "created_by_id", nullable = false)
	private User createdBy; // Admin who created the task
}
