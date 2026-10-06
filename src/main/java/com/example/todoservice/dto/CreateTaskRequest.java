package com.example.todoservice.dto;

import com.example.todoservice.entity.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CreateTaskRequest {

    @NotBlank(message="Title is required")
    @Size(max=200, message="Title must not exceed 200 characters")
    private String title;


    @Size(max=100, message="Description must not exceed 1000 characters")
    private String description;


    private Priority priority;

    @FutureOrPresent(message ="Deadline cannot be in the past")
    private LocalDate deadline;

    public CreateTaskRequest(){

    }

    public void setTitle(String title){
        this.title= title;
    }

    public String getDescription(){
        return description;
    }

    public Priority getPriority(){
        return priority;

    }

    public void setPriority(Priority priority){
        this.priority=priority;
    }

    public LocalDate getDeadline(){
        return deadline;
    }

    public void setDeadline(LocalDate deadline){
        this.deadline=deadline;
    }
}
