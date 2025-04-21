package com.projectmanagement.task.repository;

import com.projectmanagement.project.domain.Project;
import com.projectmanagement.task.domain.Task;
import com.projectmanagement.task.domain.TaskStatus;
import com.projectmanagement.user.domain.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject(Project project);
    
    List<Task> findByAssignee(User assignee);
    
    List<Task> findByProjectAndStatus(Project project, TaskStatus status);
    
    List<Task> findByAssigneeAndStatus(User assignee, TaskStatus status);
    
    List<Task> findByParentTask(Task parentTask);
    
    List<Task> findByDueDateBefore(LocalDate date);
    
    Optional<Task> findByExternalId(String externalId);
    
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND t.dueDate BETWEEN :startDate AND :endDate")
    List<Task> findTasksByProjectIdAndDateRange(Long projectId, LocalDate startDate, LocalDate endDate);
} 