package com.example.todoservice.controller;


import com.example.todoservice.dto.TaskResponse;
import com.example.todoservice.entity.Priority;
import com.example.todoservice.entity.TaskStatus;
import com.example.todoservice.exception.TaskNotFoundException;
import com.example.todoservice.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
public class TaskControllerTest {
}
