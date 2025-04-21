import React, { useState, useEffect } from 'react';
import { StyleSheet, View, ScrollView, Alert, Platform, TouchableOpacity } from 'react-native';
import { Appbar, TextInput, Button, HelperText, Chip, Divider, Text } from 'react-native-paper';
import { useNavigation, useRoute, RouteProp } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';
import DateTimePicker from '@react-native-community/datetimepicker';
import { Task, taskService, Project, projectService, User, userService } from '../services/api';

type TaskFormParams = {
  taskId?: number;
  projectId?: number;
};

type FormErrors = {
  title?: string;
  dueDate?: string;
  project?: string;
};

const TaskFormScreen: React.FC = () => {
  const navigation = useNavigation<StackNavigationProp<any>>();
  const route = useRoute<RouteProp<Record<string, TaskFormParams>, string>>();
  const { taskId, projectId } = route.params || {};
  const isEditMode = !!taskId;
  
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [status, setStatus] = useState('BACKLOG');
  const [priority, setPriority] = useState('MEDIUM');
  const [dueDate, setDueDate] = useState(new Date());
  const [showDatePicker, setShowDatePicker] = useState(false);
  const [estimatedHours, setEstimatedHours] = useState('');
  const [selectedProjectId, setSelectedProjectId] = useState<number | undefined>(projectId);
  const [selectedAssigneeId, setSelectedAssigneeId] = useState<number | undefined>(undefined);
  const [parentTaskId, setParentTaskId] = useState<number | undefined>(undefined);
  
  const [projects, setProjects] = useState<Project[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState<FormErrors>({});
  
  const statusOptions = ['BACKLOG', 'TODO', 'IN_PROGRESS', 'REVIEW', 'DONE', 'CANCELLED'];
  const priorityOptions = ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'];

  // Load projects and users for dropdown selection
  useEffect(() => {
    const fetchData = async () => {
      try {
        const [projectsRes, usersRes] = await Promise.all([
          projectService.getAll(),
          userService.getAll()
        ]);
        setProjects(projectsRes.data);
        setUsers(usersRes.data);
      } catch (error) {
        console.error('Error fetching data:', error);
        Alert.alert('Error', 'Failed to load form data');
      }
    };
    
    fetchData();
  }, []);

  // Load task data if in edit mode
  useEffect(() => {
    if (isEditMode && taskId) {
      const fetchTask = async () => {
        try {
          setLoading(true);
          const response = await taskService.getById(taskId);
          const task = response.data;
          
          setTitle(task.title);
          setDescription(task.description || '');
          setStatus(task.status);
          setPriority(task.priority);
          setDueDate(new Date(task.dueDate));
          setEstimatedHours(task.estimatedHours ? task.estimatedHours.toString() : '');
          setSelectedProjectId(task.projectId);
          setSelectedAssigneeId(task.assignee?.id);
          setParentTaskId(task.parentTaskId);
        } catch (error) {
          console.error('Error loading task:', error);
          Alert.alert('Error', 'Failed to load task data');
        } finally {
          setLoading(false);
        }
      };
      
      fetchTask();
    }
  }, [isEditMode, taskId]);

  const validateForm = (): boolean => {
    const newErrors: FormErrors = {};
    
    if (!title.trim()) {
      newErrors.title = 'Title is required';
    }
    
    if (!dueDate) {
      newErrors.dueDate = 'Due date is required';
    }
    
    if (!selectedProjectId) {
      newErrors.project = 'Project is required';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async () => {
    if (!validateForm()) {
      return;
    }
    
    try {
      setLoading(true);
      
      const taskData: Partial<Task> = {
        title,
        description,
        status,
        priority,
        dueDate: dueDate.toISOString().split('T')[0],
        estimatedHours: estimatedHours ? parseInt(estimatedHours, 10) : 0,
        projectId: selectedProjectId,
        assigneeId: selectedAssigneeId,
        parentTaskId
      };
      
      if (isEditMode && taskId) {
        await taskService.update(taskId, taskData);
        Alert.alert('Success', 'Task updated successfully');
      } else {
        await taskService.create(taskData);
        Alert.alert('Success', 'Task created successfully');
      }
      
      navigation.goBack();
    } catch (error) {
      console.error('Error saving task:', error);
      Alert.alert('Error', 'Failed to save task');
    } finally {
      setLoading(false);
    }
  };

  const onChangeDueDate = (event: any, selectedDate?: Date) => {
    setShowDatePicker(Platform.OS === 'ios');
    if (selectedDate) {
      setDueDate(selectedDate);
    }
  };

  const formatDateForDisplay = (date: Date) => {
    return date.toLocaleDateString();
  };

  return (
    <View style={styles.container}>
      <Appbar.Header>
        <Appbar.BackAction onPress={() => navigation.goBack()} />
        <Appbar.Content title={isEditMode ? "Edit Task" : "Create Task"} />
      </Appbar.Header>
      
      <ScrollView contentContainerStyle={styles.scrollContent}>
        <TextInput
          label="Title"
          value={title}
          onChangeText={setTitle}
          mode="outlined"
          style={styles.input}
          error={!!errors.title}
        />
        {errors.title && <HelperText type="error">{errors.title}</HelperText>}
        
        <TextInput
          label="Description"
          value={description}
          onChangeText={setDescription}
          mode="outlined"
          multiline
          numberOfLines={4}
          style={styles.input}
        />
        
        <Text style={styles.sectionTitle}>Status</Text>
        <View style={styles.optionsContainer}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            {statusOptions.map((option) => (
              <Chip
                key={option}
                selected={status === option}
                onPress={() => setStatus(option)}
                style={[
                  styles.chip,
                  status === option && styles.selectedChip
                ]}
                mode="outlined"
              >
                {option}
              </Chip>
            ))}
          </ScrollView>
        </View>
        
        <Text style={styles.sectionTitle}>Priority</Text>
        <View style={styles.optionsContainer}>
          {priorityOptions.map((option) => (
            <Chip
              key={option}
              selected={priority === option}
              onPress={() => setPriority(option)}
              style={[
                styles.chip,
                priority === option && styles.selectedChip
              ]}
              mode="outlined"
            >
              {option}
            </Chip>
          ))}
        </View>
        
        <Text style={styles.sectionTitle}>Due Date</Text>
        <TouchableOpacity onPress={() => setShowDatePicker(true)}>
          <View style={styles.datePickerButton}>
            <Text>{formatDateForDisplay(dueDate)}</Text>
          </View>
        </TouchableOpacity>
        {errors.dueDate && <HelperText type="error">{errors.dueDate}</HelperText>}
        
        {showDatePicker && (
          <DateTimePicker
            value={dueDate}
            mode="date"
            display="default"
            onChange={onChangeDueDate}
          />
        )}
        
        <TextInput
          label="Estimated Hours"
          value={estimatedHours}
          onChangeText={setEstimatedHours}
          mode="outlined"
          keyboardType="numeric"
          style={styles.input}
        />
        
        <Text style={styles.sectionTitle}>Project</Text>
        <View style={styles.optionsContainer}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            {projects.map((project) => (
              <Chip
                key={project.id}
                selected={selectedProjectId === project.id}
                onPress={() => setSelectedProjectId(project.id)}
                style={[
                  styles.chip,
                  selectedProjectId === project.id && styles.selectedChip
                ]}
                mode="outlined"
              >
                {project.name}
              </Chip>
            ))}
          </ScrollView>
        </View>
        {errors.project && <HelperText type="error">{errors.project}</HelperText>}
        
        <Text style={styles.sectionTitle}>Assignee</Text>
        <View style={styles.optionsContainer}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            <Chip
              selected={selectedAssigneeId === undefined}
              onPress={() => setSelectedAssigneeId(undefined)}
              style={[
                styles.chip,
                selectedAssigneeId === undefined && styles.selectedChip
              ]}
              mode="outlined"
            >
              Unassigned
            </Chip>
            
            {users.map((user) => (
              <Chip
                key={user.id}
                selected={selectedAssigneeId === user.id}
                onPress={() => setSelectedAssigneeId(user.id)}
                style={[
                  styles.chip,
                  selectedAssigneeId === user.id && styles.selectedChip
                ]}
                mode="outlined"
              >
                {`${user.firstName} ${user.lastName}`}
              </Chip>
            ))}
          </ScrollView>
        </View>
        
        <Divider style={styles.divider} />
        
        <Button
          mode="contained"
          onPress={handleSubmit}
          loading={loading}
          disabled={loading}
          style={styles.submitButton}
        >
          {isEditMode ? "Update Task" : "Create Task"}
        </Button>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
  scrollContent: {
    padding: 16,
  },
  input: {
    marginBottom: 12,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: 'bold',
    marginTop: 12,
    marginBottom: 8,
  },
  optionsContainer: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    marginBottom: 12,
  },
  chip: {
    margin: 4,
  },
  selectedChip: {
    backgroundColor: '#e0f2f1',
  },
  datePickerButton: {
    borderWidth: 1,
    borderColor: '#ccc',
    borderRadius: 4,
    padding: 12,
    marginBottom: 12,
  },
  divider: {
    marginVertical: 16,
  },
  submitButton: {
    marginTop: 8,
    marginBottom: 24,
  },
});

export default TaskFormScreen; 