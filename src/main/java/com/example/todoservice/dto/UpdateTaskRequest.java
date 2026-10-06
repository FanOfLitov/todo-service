package com.example.todoservice.dto;
import com.example.todoservice.entity.Priority;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class UpdateTaskRequest {

    @NotBlank(message="Title is required")
    @Size(max=200, message = "title must not exceed 200 characters")
    private String title;

    @Size(max=1000, message ="description must not exceed 1000 characters")
    private String description;

    private Priority priority;

    //@FutureOrPresent(message="deadline cannot be in the past")
    private LocalDate deadline;

    public UpdateTaskRequest(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title){
        this.title=title;
    }

    public String getDescription(){
        return description;

    }

    public void setDescription(String description){
        this.description=description;
    }

    public Priority getPriority(){
        return priority;
    }
    public void setPriority(Priority priority){
        this.priority=priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }


    public void setDeadline(LocalDate deadline){
        this.deadline=deadline;
    }

}
