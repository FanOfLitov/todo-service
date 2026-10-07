package com.example.todoservice.controller;

import com.example.todoservice.dto.TaskResponse;
import com.example.todoservice.entity.Priority;
import com.example.todoservice.entity.TaskStatus;
import com.example.todoservice.exception.TaskNotFoundException;
import com.example.todoservice.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Test
    void getByIdShouldReturnTask() throws Exception {
        TaskResponse response = new TaskResponse(
                1L,
                "Test task",
                "Description",
                Priority.HIGH,
                LocalDate.now().plusDays(1),
                TaskStatus.TODO,
                LocalDateTime.now()
        );

        when(taskService.getById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test task"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void getByIdShouldReturn404WhenTaskDoesNotExist() throws Exception {
        when(taskService.getById(999L))
                .thenThrow(new TaskNotFoundException(999L));

        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Task with id 999 not found"));
    }
}