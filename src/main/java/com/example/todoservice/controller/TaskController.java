package com.example.todoservice.controller;

import com.example.todoservice.dto.CreateTaskRequest;
import com.example.todoservice.dto.TaskResponse;
import com.example.todoservice.dto.UpdateTaskRequest;
import com.example.todoservice.entity.Priority;
import com.example.todoservice.entity.TaskStatus;
import com.example.todoservice.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @Valid @RequestBody CreateTaskRequest request
    ) {
        TaskResponse response = taskService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                taskService.getById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAll(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) Priority priority
    ) {
        return ResponseEntity.ok(
                taskService.getAll(status, priority)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request
    ) {
        return ResponseEntity.ok(
                taskService.update(id, request)
        );
    }

    @PatchMapping("/{id}/done")
    public ResponseEntity<TaskResponse> markAsDone(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                taskService.markAsDone(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        taskService.delete(id);

        return ResponseEntity.noContent().build();
    }
}