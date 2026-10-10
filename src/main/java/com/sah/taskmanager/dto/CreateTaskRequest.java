package com.sah.taskmanager.dto;

import com.sah.taskmanager.entity.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;

public class CreateTaskRequest {

    @Column(name = "title", nullable = false, length = 200)
    @Size(max = 200)
    @NotBlank
    private String title;

    @Column(name = "content")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status = TaskStatus.TODO;

    @Column(name = "priority", nullable = false)
    @Min(1) @Max(5)
    private short priority = 3;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "due_date")
    private LocalDate dueDate;

    public CreateTaskRequest(String title, String content, TaskStatus status, short priority, LocalDate dueDate){
        this.title = title;
        this.content = content;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public short getPriority() {
        return priority;
    }

    public void setPriority(short priority) {
        this.priority = priority;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


}
