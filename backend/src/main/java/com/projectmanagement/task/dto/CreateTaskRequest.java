package com.projectmanagement.task.dto;

import com.projectmanagement.task.domain.TaskPriority;
import com.projectmanagement.task.domain.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {
    @NotBlank(message = "Task title is required")
    private String title;
    
    private String description;
    
    @NotNull(message = "Task status is required")
    private TaskStatus status;
    
    @NotNull(message = "Task priority is required")
    private TaskPriority priority;
    
    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be in the future or present")
    private LocalDate dueDate;
    
    private Integer estimatedHours;
    
    @NotNull(message = "Project ID is required")
    private Long projectId;
    
    private Long assigneeId;
    
    private Long parentTaskId;
} 