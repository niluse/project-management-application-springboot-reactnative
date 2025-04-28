-- Users Table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    keycloak_id VARCHAR(50),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(50),
    updated_at TIMESTAMP NOT NULL,
    updated_by VARCHAR(50)
);

-- Projects Table
CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    target_end_date DATE,
    actual_end_date DATE,
    status VARCHAR(20) NOT NULL,
    estimated_effort_hours INTEGER DEFAULT 0,
    actual_effort_hours INTEGER,
    external_id VARCHAR(50),
    project_manager_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(50),
    updated_at TIMESTAMP NOT NULL,
    updated_by VARCHAR(50)
);

-- Project Team (Junction Table)
CREATE TABLE project_team (
    project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (project_id, user_id)
);

-- Tasks Table
CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    due_date DATE NOT NULL,
    estimated_hours INTEGER DEFAULT 0,
    actual_hours INTEGER,
    external_id VARCHAR(50),
    project_id BIGINT NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    assignee_id BIGINT REFERENCES users(id),
    parent_task_id BIGINT REFERENCES tasks(id),
    created_at TIMESTAMP NOT NULL,
    created_by VARCHAR(50),
    updated_at TIMESTAMP NOT NULL,
    updated_by VARCHAR(50)
);

-- Create indexes for performance
CREATE INDEX idx_tasks_project_id ON tasks(project_id);
CREATE INDEX idx_tasks_assignee_id ON tasks(assignee_id);
CREATE INDEX idx_tasks_parent_task_id ON tasks(parent_task_id);
CREATE INDEX idx_tasks_status ON tasks(status);
CREATE INDEX idx_tasks_due_date ON tasks(due_date);
CREATE INDEX idx_projects_status ON projects(status);
CREATE INDEX idx_projects_project_manager_id ON projects(project_manager_id); 