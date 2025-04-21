package com.projectmanagement.project.controller;

import com.projectmanagement.project.domain.ProjectStatus;
import com.projectmanagement.project.dto.CreateProjectRequest;
import com.projectmanagement.project.dto.ProjectDTO;
import com.projectmanagement.project.service.ProjectService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody CreateProjectRequest request) {
        return new ResponseEntity<>(projectService.createProject(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects(
            @RequestParam(required = false) ProjectStatus status,
            @RequestParam(required = false) Long projectManagerId,
            @RequestParam(required = false) Long teamMemberId) {
        
        if (status != null) {
            return ResponseEntity.ok(projectService.getProjectsByStatus(status));
        } else if (projectManagerId != null) {
            return ResponseEntity.ok(projectService.getProjectsByProjectManager(projectManagerId));
        } else if (teamMemberId != null) {
            return ResponseEntity.ok(projectService.getProjectsByTeamMember(teamMemberId));
        } else {
            return ResponseEntity.ok(projectService.getAllProjects());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @Valid @RequestBody CreateProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ProjectDTO> updateProjectStatus(@PathVariable Long id, @RequestParam ProjectStatus status) {
        return ResponseEntity.ok(projectService.updateProjectStatus(id, status));
    }

    @PutMapping("/{projectId}/team/{userId}")
    public ResponseEntity<ProjectDTO> addTeamMember(@PathVariable Long projectId, @PathVariable Long userId) {
        return ResponseEntity.ok(projectService.addTeamMember(projectId, userId));
    }

    @DeleteMapping("/{projectId}/team/{userId}")
    public ResponseEntity<ProjectDTO> removeTeamMember(@PathVariable Long projectId, @PathVariable Long userId) {
        return ResponseEntity.ok(projectService.removeTeamMember(projectId, userId));
    }
} 