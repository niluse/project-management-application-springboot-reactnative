package com.projectmanagement.task.controller;

import com.projectmanagement.task.domain.TaskStatus;
import com.projectmanagement.task.dto.CreateTaskRequest;
import com.projectmanagement.task.dto.TaskDTO;
import com.projectmanagement.task.service.TaskService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody CreateTaskRequest request) {
        return new ResponseEntity<>(taskService.createTask(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getTasks(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) TaskStatus status) {
        
        if (projectId != null && status != null) {
            return ResponseEntity.ok(taskService.getTasksByProjectAndStatus(projectId, status));
        } else if (assigneeId != null && status != null) {
            return ResponseEntity.ok(taskService.getTasksByAssigneeAndStatus(assigneeId, status));
        } else if (projectId != null) {
            return ResponseEntity.ok(taskService.getTasksByProject(projectId));
        } else if (assigneeId != null) {
            return ResponseEntity.ok(taskService.getTasksByAssignee(assigneeId));
        } else {
            return ResponseEntity.ok(taskService.getAllTasks());
        }
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TaskDTO>> getOverdueTasks() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }

    @GetMapping("/subtasks/{parentTaskId}")
    public ResponseEntity<List<TaskDTO>> getSubtasks(@PathVariable Long parentTaskId) {
        return ResponseEntity.ok(taskService.getSubtasks(parentTaskId));
    }

    @GetMapping("/project/{projectId}/dateRange")
    public ResponseEntity<List<TaskDTO>> getTasksByProjectAndDateRange(
            @PathVariable Long projectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(taskService.getTasksByProjectAndDateRange(projectId, startDate, endDate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody CreateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(@PathVariable Long id, @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, status));
    }

    @PutMapping("/{id}/assign/{assigneeId}")
    public ResponseEntity<TaskDTO> assignTask(@PathVariable Long id, @PathVariable Long assigneeId) {
        return ResponseEntity.ok(taskService.assignTask(id, assigneeId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
} 