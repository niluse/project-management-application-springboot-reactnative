package com.projectmanagement.task.service;

import com.projectmanagement.project.domain.Project;
import com.projectmanagement.project.repository.ProjectRepository;
import com.projectmanagement.task.domain.Task;
import com.projectmanagement.task.domain.TaskStatus;
import com.projectmanagement.task.dto.CreateTaskRequest;
import com.projectmanagement.task.dto.TaskDTO;
import com.projectmanagement.task.repository.TaskRepository;
import com.projectmanagement.user.domain.User;
import com.projectmanagement.user.dto.UserDTO;
import com.projectmanagement.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public TaskDTO createTask(CreateTaskRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + request.getProjectId()));
        
        Task.TaskBuilder taskBuilder = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .estimatedHours(request.getEstimatedHours())
                .project(project);
        
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found with id: " + request.getAssigneeId()));
            taskBuilder.assignee(assignee);
        }
        
        if (request.getParentTaskId() != null) {
            Task parentTask = taskRepository.findById(request.getParentTaskId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent task not found with id: " + request.getParentTaskId()));
            taskBuilder.parentTask(parentTask);
        }
        
        Task task = taskBuilder.build();
        return mapToDTO(taskRepository.save(task));
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        
        return taskRepository.findByProject(project).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByAssignee(Long assigneeId) {
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + assigneeId));
        
        return taskRepository.findByAssignee(assignee).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByProjectAndStatus(Long projectId, TaskStatus status) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        
        return taskRepository.findByProjectAndStatus(project, status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByAssigneeAndStatus(Long assigneeId, TaskStatus status) {
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + assigneeId));
        
        return taskRepository.findByAssigneeAndStatus(assignee, status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getSubtasks(Long parentTaskId) {
        Task parentTask = taskRepository.findById(parentTaskId)
                .orElseThrow(() -> new EntityNotFoundException("Parent task not found with id: " + parentTaskId));
        
        return taskRepository.findByParentTask(parentTask).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getOverdueTasks() {
        return taskRepository.findByDueDateBefore(LocalDate.now()).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TaskDTO updateTask(Long id, CreateTaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + request.getProjectId()));
        
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setEstimatedHours(request.getEstimatedHours());
        task.setProject(project);
        
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new EntityNotFoundException("Assignee not found with id: " + request.getAssigneeId()));
            task.setAssignee(assignee);
        } else {
            task.setAssignee(null);
        }
        
        if (request.getParentTaskId() != null) {
            if (request.getParentTaskId().equals(id)) {
                throw new IllegalArgumentException("Task cannot be its own parent");
            }
            
            Task parentTask = taskRepository.findById(request.getParentTaskId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent task not found with id: " + request.getParentTaskId()));
            task.setParentTask(parentTask);
        } else {
            task.setParentTask(null);
        }
        
        return mapToDTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDTO updateTaskStatus(Long id, TaskStatus status) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        
        task.setStatus(status);
        
        return mapToDTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public TaskDTO assignTask(Long id, Long assigneeId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
        
        User assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + assigneeId));
        
        task.setAssignee(assignee);
        
        return mapToDTO(taskRepository.save(task));
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
        
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByProjectAndDateRange(Long projectId, LocalDate startDate, LocalDate endDate) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Project not found with id: " + projectId);
        }
        
        return taskRepository.findTasksByProjectIdAndDateRange(projectId, startDate, endDate).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    
    private TaskDTO mapToDTO(Task task) {
        TaskDTO dto = TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .estimatedHours(task.getEstimatedHours())
                .actualHours(task.getActualHours())
                .externalId(task.getExternalId())
                .projectId(task.getProject().getId())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
        
        if (task.getAssignee() != null) {
            dto.setAssignee(mapUserToDTO(task.getAssignee()));
        }
        
        if (task.getParentTask() != null) {
            dto.setParentTaskId(task.getParentTask().getId());
        }
        
        return dto;
    }
    
    private UserDTO mapUserToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }
} 