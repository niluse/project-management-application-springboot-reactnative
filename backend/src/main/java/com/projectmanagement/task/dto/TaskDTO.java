package com.projectmanagement.task.dto;

import com.projectmanagement.task.domain.TaskPriority;
import com.projectmanagement.task.domain.TaskStatus;
import com.projectmanagement.user.dto.UserDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
    private Integer estimatedHours;
    private Integer actualHours;
    private String externalId;
    private Long projectId;
    private UserDTO assignee;
    private Long parentTaskId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 