package com.projectmanagement.project.service;

import com.projectmanagement.project.domain.Project;
import com.projectmanagement.project.domain.ProjectStatus;
import com.projectmanagement.project.dto.CreateProjectRequest;
import com.projectmanagement.project.dto.ProjectDTO;
import com.projectmanagement.project.repository.ProjectRepository;
import com.projectmanagement.user.domain.User;
import com.projectmanagement.user.dto.UserDTO;
import com.projectmanagement.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProjectDTO createProject(CreateProjectRequest request) {
        User projectManager = userRepository.findById(request.getProjectManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Project manager not found with id: " + request.getProjectManagerId()));
        
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .targetEndDate(request.getTargetEndDate())
                .status(request.getStatus())
                .estimatedEffortHours(request.getEstimatedEffortHours())
                .projectManager(projectManager)
                .teamMembers(new HashSet<>())
                .build();
        
        if (request.getTeamMemberIds() != null && !request.getTeamMemberIds().isEmpty()) {
            Set<User> teamMembers = userRepository.findAllById(request.getTeamMemberIds())
                    .stream()
                    .collect(Collectors.toSet());
            
            if (teamMembers.size() != request.getTeamMemberIds().size()) {
                throw new EntityNotFoundException("One or more team members not found");
            }
            
            project.setTeamMembers(teamMembers);
        }
        
        return mapToDTO(projectRepository.save(project));
    }

    @Override
    @Transactional(readOnly = true)
    public ProjectDTO getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByStatus(ProjectStatus status) {
        return projectRepository.findByStatus(status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByProjectManager(Long projectManagerId) {
        User projectManager = userRepository.findById(projectManagerId)
                .orElseThrow(() -> new EntityNotFoundException("Project manager not found with id: " + projectManagerId));
        
        return projectRepository.findByProjectManager(projectManager).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectDTO> getProjectsByTeamMember(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        
        return projectRepository.findByTeamMemberId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProjectDTO updateProject(Long id, CreateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        
        User projectManager = userRepository.findById(request.getProjectManagerId())
                .orElseThrow(() -> new EntityNotFoundException("Project manager not found with id: " + request.getProjectManagerId()));
        
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setStartDate(request.getStartDate());
        project.setTargetEndDate(request.getTargetEndDate());
        project.setStatus(request.getStatus());
        project.setEstimatedEffortHours(request.getEstimatedEffortHours());
        project.setProjectManager(projectManager);
        
        if (request.getTeamMemberIds() != null) {
            Set<User> teamMembers = userRepository.findAllById(request.getTeamMemberIds())
                    .stream()
                    .collect(Collectors.toSet());
            
            if (teamMembers.size() != request.getTeamMemberIds().size()) {
                throw new EntityNotFoundException("One or more team members not found");
            }
            
            project.setTeamMembers(teamMembers);
        }
        
        return mapToDTO(projectRepository.save(project));
    }

    @Override
    @Transactional
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new EntityNotFoundException("Project not found with id: " + id);
        }
        
        projectRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ProjectDTO addTeamMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        project.getTeamMembers().add(user);
        
        return mapToDTO(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectDTO removeTeamMember(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        
        project.getTeamMembers().remove(user);
        
        return mapToDTO(projectRepository.save(project));
    }

    @Override
    @Transactional
    public ProjectDTO updateProjectStatus(Long id, ProjectStatus status) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        
        project.setStatus(status);
        
        return mapToDTO(projectRepository.save(project));
    }
    
    private ProjectDTO mapToDTO(Project project) {
        ProjectDTO dto = ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .startDate(project.getStartDate())
                .targetEndDate(project.getTargetEndDate())
                .actualEndDate(project.getActualEndDate())
                .status(project.getStatus())
                .estimatedEffortHours(project.getEstimatedEffortHours())
                .actualEffortHours(project.getActualEffortHours())
                .externalId(project.getExternalId())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
        
        if (project.getProjectManager() != null) {
            dto.setProjectManager(mapUserToDTO(project.getProjectManager()));
        }
        
        if (project.getTeamMembers() != null && !project.getTeamMembers().isEmpty()) {
            dto.setTeamMembers(project.getTeamMembers().stream()
                    .map(this::mapUserToDTO)
                    .collect(Collectors.toSet()));
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