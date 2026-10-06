package com.example.todoservice.service;

import com.example.todoservice.dto.CreateTaskRequest;
import com.example.todoservice.dto.TaskResponse;
import com.example.todoservice.dto.UpdateTaskRequest;
import com.example.todoservice.entity.Priority;
import com.example.todoservice.entity.Task;
import com.example.todoservice.entity.TaskStatus;
import com.example.todoservice.exception.TaskNotFoundException;
import com.example.todoservice.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test task");
        task.setDescription("Test description");
        task.setPriority(Priority.HIGH);
        task.setDeadline(LocalDate.now().plusDays(1));
        task.setStatus(TaskStatus.TODO);
    }

    @Test
    void createShouldSaveTaskAndReturnResponse() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("New task");
        request.setDescription("Description");
        request.setPriority(Priority.HIGH);
        request.setDeadline(LocalDate.now().plusDays(2));

        when(taskRepository.save(any(Task.class)))
                .thenAnswer(invocation -> {
                    Task savedTask = invocation.getArgument(0);
                    savedTask.setId(1L);
                    savedTask.prePersist();
                    return savedTask;
                });

        TaskResponse response = taskService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("New task", response.getTitle());
        assertEquals(Priority.HIGH, response.getPriority());
        assertEquals(TaskStatus.TODO, response.getStatus());

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void getByIdShouldReturnTaskWhenTaskExists() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        TaskResponse response = taskService.getById(1L);

        assertEquals(1L, response.getId());
        assertEquals("Test task", response.getTitle());
        assertEquals(Priority.HIGH, response.getPriority());

        verify(taskRepository).findById(1L);
    }

    @Test
    void getByIdShouldThrowExceptionWhenTaskDoesNotExist() {
        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        TaskNotFoundException exception = assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getById(999L)
        );

        assertEquals(
                "Task with id 999 not found",
                exception.getMessage()
        );

        verify(taskRepository).findById(999L);
    }

    @Test
    void markAsDoneShouldChangeStatusToDone() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(task))
                .thenReturn(task);

        TaskResponse response = taskService.markAsDone(1L);

        assertEquals(TaskStatus.DONE, response.getStatus());

        verify(taskRepository).save(task);
    }

    @Test
    void deleteShouldDeleteExistingTask() {
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        taskService.delete(1L);

        verify(taskRepository).delete(task);
    }

    @Test
    void updateShouldChangeTaskFields() {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Updated title");
        request.setDescription("Updated description");
        request.setPriority(Priority.MEDIUM);
        request.setDeadline(LocalDate.now().plusDays(5));

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(taskRepository.save(task))
                .thenReturn(task);

        TaskResponse response = taskService.update(1L, request);

        assertEquals("Updated title", response.getTitle());
        assertEquals("Updated description", response.getDescription());
        assertEquals(Priority.MEDIUM, response.getPriority());

        verify(taskRepository).save(task);
    }

    @Test
    void getAllShouldFilterAndSortTasks() {
        Task highSoon = createTask(
                1L,
                "High soon",
                Priority.HIGH,
                TaskStatus.TODO,
                LocalDate.now().plusDays(1)
        );

        Task lowSoon = createTask(
                2L,
                "Low soon",
                Priority.LOW,
                TaskStatus.TODO,
                LocalDate.now().plusDays(1)
        );

        Task highLater = createTask(
                3L,
                "High later",
                Priority.HIGH,
                TaskStatus.TODO,
                LocalDate.now().plusDays(5)
        );

        Task doneTask = createTask(
                4L,
                "Done",
                Priority.HIGH,
                TaskStatus.DONE,
                LocalDate.now()
        );

        when(taskRepository.findAll())
                .thenReturn(List.of(
                        highLater,
                        lowSoon,
                        doneTask,
                        highSoon
                ));

        List<TaskResponse> result =
                taskService.getAll(TaskStatus.TODO, null);

        assertEquals(3, result.size());

        assertEquals("High soon", result.get(0).getTitle());
        assertEquals("Low soon", result.get(1).getTitle());
        assertEquals("High later", result.get(2).getTitle());
    }

    private Task createTask(
            Long id,
            String title,
            Priority priority,
            TaskStatus status,
            LocalDate deadline
    ) {
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription("Description");
        task.setPriority(priority);
        task.setStatus(status);
        task.setDeadline(deadline);

        return task;
    }
}