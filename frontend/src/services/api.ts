import axios from 'axios';
import AsyncStorage from '@react-native-async-storage/async-storage';

// Base URL for API requests
const API_URL = 'http://10.0.2.2:8080/api';

// Create an axios instance
const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add a request interceptor to add auth token to requests
api.interceptors.request.use(
  async (config) => {
    const token = await AsyncStorage.getItem('auth_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add a response interceptor to handle token expiration
api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    
    // If the error is 401 and not already retrying
    if (error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        // Here you would handle the token refresh logic
        // For now, we'll just redirect to login
        await AsyncStorage.removeItem('auth_token');
        // Navigation to login would happen at the component level
        return Promise.reject(error);
      } catch (refreshError) {
        return Promise.reject(refreshError);
      }
    }
    
    return Promise.reject(error);
  }
);

// API interfaces for typesafety
export interface User {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
  active: boolean;
}

export interface Project {
  id: number;
  name: string;
  description: string;
  startDate: string;
  targetEndDate: string;
  actualEndDate: string;
  status: string;
  estimatedEffortHours: number;
  actualEffortHours: number;
  projectManager: User;
  teamMembers: User[];
}

export interface Task {
  id: number;
  title: string;
  description: string;
  status: string;
  priority: string;
  dueDate: string;
  estimatedHours: number;
  actualHours: number;
  projectId: number;
  assignee: User;
  parentTaskId: number;
}

// User service
export const userService = {
  getAll: () => api.get<User[]>('/users'),
  getById: (id: number) => api.get<User>(`/users/${id}`),
  create: (user: Partial<User>) => api.post<User>('/users', user),
  update: (id: number, user: Partial<User>) => api.put<User>(`/users/${id}`, user),
  delete: (id: number) => api.delete(`/users/${id}`),
};

// Project service
export const projectService = {
  getAll: () => api.get<Project[]>('/projects'),
  getById: (id: number) => api.get<Project>(`/projects/${id}`),
  getByStatus: (status: string) => api.get<Project[]>(`/projects?status=${status}`),
  getByManager: (managerId: number) => api.get<Project[]>(`/projects?projectManagerId=${managerId}`),
  getByTeamMember: (userId: number) => api.get<Project[]>(`/projects?teamMemberId=${userId}`),
  create: (project: Partial<Project>) => api.post<Project>('/projects', project),
  update: (id: number, project: Partial<Project>) => api.put<Project>(`/projects/${id}`, project),
  updateStatus: (id: number, status: string) => api.put<Project>(`/projects/${id}/status?status=${status}`),
  addTeamMember: (projectId: number, userId: number) => api.put<Project>(`/projects/${projectId}/team/${userId}`),
  removeTeamMember: (projectId: number, userId: number) => api.delete<Project>(`/projects/${projectId}/team/${userId}`),
  delete: (id: number) => api.delete(`/projects/${id}`),
};

// Task service
export const taskService = {
  getAll: () => api.get<Task[]>('/tasks'),
  getById: (id: number) => api.get<Task>(`/tasks/${id}`),
  getByProject: (projectId: number) => api.get<Task[]>(`/tasks?projectId=${projectId}`),
  getByAssignee: (assigneeId: number) => api.get<Task[]>(`/tasks?assigneeId=${assigneeId}`),
  getByProjectAndStatus: (projectId: number, status: string) => api.get<Task[]>(`/tasks?projectId=${projectId}&status=${status}`),
  getByAssigneeAndStatus: (assigneeId: number, status: string) => api.get<Task[]>(`/tasks?assigneeId=${assigneeId}&status=${status}`),
  getOverdue: () => api.get<Task[]>('/tasks/overdue'),
  getSubtasks: (parentTaskId: number) => api.get<Task[]>(`/tasks/subtasks/${parentTaskId}`),
  getByProjectAndDateRange: (projectId: number, startDate: string, endDate: string) => 
    api.get<Task[]>(`/tasks/project/${projectId}/dateRange?startDate=${startDate}&endDate=${endDate}`),
  create: (task: Partial<Task>) => api.post<Task>('/tasks', task),
  update: (id: number, task: Partial<Task>) => api.put<Task>(`/tasks/${id}`, task),
  updateStatus: (id: number, status: string) => api.put<Task>(`/tasks/${id}/status?status=${status}`),
  assign: (id: number, assigneeId: number) => api.put<Task>(`/tasks/${id}/assign/${assigneeId}`),
  delete: (id: number) => api.delete(`/tasks/${id}`),
};

export default api; 