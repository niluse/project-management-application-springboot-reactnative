package com.projectmanagement.task.service;

import com.projectmanagement.task.domain.TaskStatus;
import com.projectmanagement.task.dto.CreateTaskRequest;
import com.projectmanagement.task.dto.TaskDTO;
import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskDTO createTask(CreateTaskRequest request);
    
    TaskDTO getTaskById(Long id);
    
    List<TaskDTO> getAllTasks();
    
    List<TaskDTO> getTasksByProject(Long projectId);
    
    List<TaskDTO> getTasksByAssignee(Long assigneeId);
    
    List<TaskDTO> getTasksByProjectAndStatus(Long projectId, TaskStatus status);
    
    List<TaskDTO> getTasksByAssigneeAndStatus(Long assigneeId, TaskStatus status);
    
    List<TaskDTO> getSubtasks(Long parentTaskId);
    
    List<TaskDTO> getOverdueTasks();
    
    TaskDTO updateTask(Long id, CreateTaskRequest request);
    
    TaskDTO updateTaskStatus(Long id, TaskStatus status);
    
    TaskDTO assignTask(Long id, Long assigneeId);
    
    void deleteTask(Long id);
    
    List<TaskDTO> getTasksByProjectAndDateRange(Long projectId, LocalDate startDate, LocalDate endDate);
} 