package com.projectmanagement.project.dto;

import com.projectmanagement.project.domain.ProjectStatus;
import com.projectmanagement.user.dto.UserDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate targetEndDate;
    private LocalDate actualEndDate;
    private ProjectStatus status;
    private Integer estimatedEffortHours;
    private Integer actualEffortHours;
    private String externalId;
    private UserDTO projectManager;
    
    @Builder.Default
    private Set<UserDTO> teamMembers = new HashSet<>();
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 