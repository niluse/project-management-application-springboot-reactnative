import React, { useState, useEffect, useCallback } from 'react';
import { StyleSheet, View, FlatList, RefreshControl, ActivityIndicator } from 'react-native';
import { Appbar, FAB, Searchbar, Chip, Text } from 'react-native-paper';
import { useFocusEffect, useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';
import TaskCard from '../components/TaskCard';
import { Task, taskService } from '../services/api';

type TaskListScreenProps = {
  projectId?: number;
};

const TaskListScreen: React.FC<TaskListScreenProps> = ({ projectId }) => {
  const navigation = useNavigation<StackNavigationProp<any>>();
  const [tasks, setTasks] = useState<Task[]>([]);
  const [filteredTasks, setFilteredTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [statusFilter, setStatusFilter] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);

  const statusOptions = ['BACKLOG', 'TODO', 'IN_PROGRESS', 'REVIEW', 'DONE', 'CANCELLED'];

  const fetchTasks = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      
      let response;
      if (projectId) {
        response = await taskService.getByProject(projectId);
      } else {
        response = await taskService.getAll();
      }
      
      setTasks(response.data);
      setFilteredTasks(response.data);
    } catch (err) {
      console.error('Error fetching tasks:', err);
      setError('Failed to load tasks. Please try again.');
    } finally {
      setLoading(false);
    }
  }, [projectId]);

  // Fetch tasks when the screen comes into focus
  useFocusEffect(
    useCallback(() => {
      fetchTasks();
    }, [fetchTasks])
  );

  const onRefresh = async () => {
    setRefreshing(true);
    await fetchTasks();
    setRefreshing(false);
  };

  // Apply filters and search
  useEffect(() => {
    let result = tasks;
    
    // Apply status filter
    if (statusFilter) {
      result = result.filter(task => task.status === statusFilter);
    }
    
    // Apply search query
    if (searchQuery) {
      const query = searchQuery.toLowerCase();
      result = result.filter(task => 
        task.title.toLowerCase().includes(query) || 
        (task.description && task.description.toLowerCase().includes(query))
      );
    }
    
    setFilteredTasks(result);
  }, [tasks, statusFilter, searchQuery]);

  const handleTaskPress = (task: Task) => {
    navigation.navigate('TaskDetail', { taskId: task.id });
  };

  const handleCreateTask = () => {
    navigation.navigate('TaskForm', { projectId });
  };

  const renderTaskItem = ({ item }: { item: Task }) => (
    <TaskCard task={item} onPress={handleTaskPress} />
  );

  return (
    <View style={styles.container}>
      <Appbar.Header>
        <Appbar.BackAction onPress={() => navigation.goBack()} />
        <Appbar.Content title={projectId ? "Project Tasks" : "All Tasks"} />
        <Appbar.Action icon="filter" onPress={() => {}} />
      </Appbar.Header>
      
      <View style={styles.searchContainer}>
        <Searchbar
          placeholder="Search tasks..."
          onChangeText={setSearchQuery}
          value={searchQuery}
          style={styles.searchBar}
        />
      </View>
      
      <View style={styles.filterContainer}>
        <Text style={styles.filterLabel}>Filter by status:</Text>
        <FlatList
          horizontal
          data={statusOptions}
          keyExtractor={(item) => item}
          renderItem={({ item }) => (
            <Chip
              selected={statusFilter === item}
              onPress={() => setStatusFilter(statusFilter === item ? null : item)}
              style={[
                styles.statusChip, 
                statusFilter === item && styles.selectedChip
              ]}
              mode="outlined"
            >
              {item}
            </Chip>
          )}
          showsHorizontalScrollIndicator={false}
          contentContainerStyle={styles.chipContainer}
        />
      </View>
      
      {loading && !refreshing ? (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" />
        </View>
      ) : error ? (
        <View style={styles.errorContainer}>
          <Text style={styles.errorText}>{error}</Text>
        </View>
      ) : (
        <FlatList
          data={filteredTasks}
          keyExtractor={(item) => item.id.toString()}
          renderItem={renderTaskItem}
          contentContainerStyle={styles.listContent}
          refreshControl={
            <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
          }
          ListEmptyComponent={() => (
            <View style={styles.emptyContainer}>
              <Text style={styles.emptyText}>No tasks found</Text>
            </View>
          )}
        />
      )}
      
      <FAB
        style={styles.fab}
        icon="plus"
        onPress={handleCreateTask}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  searchContainer: {
    padding: 10,
  },
  searchBar: {
    elevation: 2,
  },
  filterContainer: {
    paddingHorizontal: 10,
    marginBottom: 10,
  },
  filterLabel: {
    marginBottom: 8,
    fontWeight: 'bold',
  },
  chipContainer: {
    paddingVertical: 5,
  },
  statusChip: {
    marginRight: 8,
  },
  selectedChip: {
    backgroundColor: '#e0f2f1',
  },
  listContent: {
    padding: 10,
  },
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  errorContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 20,
  },
  errorText: {
    color: '#f44336',
    textAlign: 'center',
  },
  emptyContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    padding: 40,
  },
  emptyText: {
    fontSize: 16,
    color: '#9e9e9e',
  },
  fab: {
    position: 'absolute',
    margin: 16,
    right: 0,
    bottom: 0,
    backgroundColor: '#2196F3',
  },
});

export default TaskListScreen; 