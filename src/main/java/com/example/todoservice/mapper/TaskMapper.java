package com.example.todoservice.mapper;

import com.example.todoservice.dto.CreateTaskRequest;
import com.example.todoservice.dto.TaskResponse;
import com.example.todoservice.entity.Priority;
import com.example.todoservice.entity.Task;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static Task toEntity(CreateTaskRequest request) {
        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(
                request.getPriority() != null
                        ? request.getPriority()
                        : Priority.MEDIUM
        );
        task.setDeadline(request.getDeadline());

        return task;
    }

    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getDeadline(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }
}