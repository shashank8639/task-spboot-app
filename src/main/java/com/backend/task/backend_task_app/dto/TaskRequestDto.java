package com.backend.task.backend_task_app.dto;

import lombok.Data;

@Data
public class TaskRequestDto {

	private String taskName;
	private int duration;
	private boolean isCompleted;
	private String assignedTo;
}
