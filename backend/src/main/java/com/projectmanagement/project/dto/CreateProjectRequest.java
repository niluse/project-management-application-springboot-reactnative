package com.projectmanagement.project.dto;

import com.projectmanagement.project.domain.ProjectStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {
    @NotBlank(message = "Project name is required")
    private String name;
    
    private String description;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @FutureOrPresent(message = "Target end date must be in the future or present")
    private LocalDate targetEndDate;
    
    @NotNull(message = "Status is required")
    private ProjectStatus status;
    
    private Integer estimatedEffortHours;
    
    @NotNull(message = "Project manager ID is required")
    private Long projectManagerId;
    
    private Set<Long> teamMemberIds;
} 