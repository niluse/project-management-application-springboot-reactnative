package com.projectmanagement.project.service;

import com.projectmanagement.project.domain.ProjectStatus;
import com.projectmanagement.project.dto.CreateProjectRequest;
import com.projectmanagement.project.dto.ProjectDTO;
import java.util.List;

public interface ProjectService {
    ProjectDTO createProject(CreateProjectRequest request);
    
    ProjectDTO getProjectById(Long id);
    
    List<ProjectDTO> getAllProjects();
    
    List<ProjectDTO> getProjectsByStatus(ProjectStatus status);
    
    List<ProjectDTO> getProjectsByProjectManager(Long projectManagerId);
    
    List<ProjectDTO> getProjectsByTeamMember(Long userId);
    
    ProjectDTO updateProject(Long id, CreateProjectRequest request);
    
    void deleteProject(Long id);
    
    ProjectDTO addTeamMember(Long projectId, Long userId);
    
    ProjectDTO removeTeamMember(Long projectId, Long userId);
    
    ProjectDTO updateProjectStatus(Long id, ProjectStatus status);
} 