package com.example.todoservice.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name ="task")
public class Task {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length=200)
    private String title;

    @Column(nullable = false, length=200)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Priority priority =Priority.MEDIUM;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable= false)
    private TaskStatus status = TaskStatus.TODO;

    @Column(nullable =false, updatable=false)
    private LocalDateTime createdAt;

    public Task(){}


    @PrePersist
    public void prePersist(){
        if(createdAt==null){
            createdAt= LocalDateTime.now();
        }
    }

    public Long getId(){
        return id;


    }

    public void setId(Long id){
        this.id=id;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(){
        this.title=title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description=description;

    }



    public Priority getPriority() {
        return priority;
    }

    public void setPriority(){
        this.priority=priority;
    }

    public LocalDate getDeadline(){
        return deadline;
    }

    public void setDeadline(LocalDate deadline){
        this.deadline = deadline;
    }

    public TaskStatus getStatus(){
        return status;
    }

    public void setStatus(TaskStatus status){
        this.status =status;
    }

    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
}
