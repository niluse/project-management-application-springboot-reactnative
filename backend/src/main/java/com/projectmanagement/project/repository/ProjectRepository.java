package com.projectmanagement.project.repository;

import com.projectmanagement.project.domain.Project;
import com.projectmanagement.project.domain.ProjectStatus;
import com.projectmanagement.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByProjectManager(User projectManager);
    
    List<Project> findByStatus(ProjectStatus status);
    
    @Query("SELECT p FROM Project p JOIN p.teamMembers tm WHERE tm.id = :userId")
    List<Project> findByTeamMemberId(Long userId);
    
    Optional<Project> findByExternalId(String externalId);
} 