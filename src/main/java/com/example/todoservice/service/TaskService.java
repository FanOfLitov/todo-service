package com.example.todoservice.service;

import com.example.todoservice.dto.CreateTaskRequest;
import com.example.todoservice.dto.TaskResponse;
import com.example.todoservice.dto.UpdateTaskRequest;
import com.example.todoservice.entity.Priority;
import com.example.todoservice.entity.Task;
import com.example.todoservice.entity.TaskStatus;
import com.example.todoservice.exception.TaskNotFoundException;
import com.example.todoservice.mapper.TaskMapper;
import com.example.todoservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse create(CreateTaskRequest request) {
        Task task = TaskMapper.toEntity(request);

        Task savedTask = taskRepository.save(task);

        return TaskMapper.toResponse(savedTask);
    }

    public TaskResponse getById(Long id) {
        Task task = findTaskById(id);

        return TaskMapper.toResponse(task);
    }

    public List<TaskResponse> getAll(
            TaskStatus status,
            Priority priority
    ) {
        return taskRepository.findAll()
                .stream()
                .filter(task ->
                        status == null || task.getStatus() == status
                )
                .filter(task ->
                        priority == null || task.getPriority() == priority
                )
                .sorted(taskComparator())
                .map(TaskMapper::toResponse)
                .toList();
    }

    public TaskResponse update(
            Long id,
            UpdateTaskRequest request
    ) {
        Task task = findTaskById(id);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setPriority(
                request.getPriority() != null
                        ? request.getPriority()
                        : Priority.MEDIUM
        );
        task.setDeadline(request.getDeadline());

        Task updatedTask = taskRepository.save(task);

        return TaskMapper.toResponse(updatedTask);
    }

    public TaskResponse markAsDone(Long id) {
        Task task = findTaskById(id);

        task.setStatus(TaskStatus.DONE);

        Task updatedTask = taskRepository.save(task);

        return TaskMapper.toResponse(updatedTask);
    }

    public void delete(Long id) {
        Task task = findTaskById(id);

        taskRepository.delete(task);
    }

    private Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    private Comparator<Task> taskComparator() {
        return Comparator
                .comparing(
                        Task::getDeadline,
                        Comparator.nullsLast(
                                Comparator.naturalOrder()
                        )
                )
                .thenComparingInt(
                        task -> priorityOrder(task.getPriority())
                );
    }

    private int priorityOrder(Priority priority) {
        return switch (priority) {
            case HIGH -> 0;
            case MEDIUM -> 1;
            case LOW -> 2;
        };
    }
}