import React from 'react';
import { StyleSheet, View, TouchableOpacity } from 'react-native';
import { Card, Title, Paragraph, Chip, Avatar, Text } from 'react-native-paper';
import { Task } from '../services/api';

interface TaskCardProps {
  task: Task;
  onPress: (task: Task) => void;
}

// Map task status to colors
const getStatusColor = (status: string) => {
  switch (status) {
    case 'BACKLOG':
      return '#9E9E9E'; // Gray
    case 'TODO':
      return '#2196F3'; // Blue
    case 'IN_PROGRESS':
      return '#FF9800'; // Orange
    case 'REVIEW':
      return '#673AB7'; // Deep Purple
    case 'DONE':
      return '#4CAF50'; // Green
    case 'CANCELLED':
      return '#F44336'; // Red
    default:
      return '#9E9E9E'; // Gray
  }
};

// Map priority to colors and icons
const getPriorityStyle = (priority: string) => {
  switch (priority) {
    case 'LOW':
      return { color: '#4CAF50', icon: 'chevron-down' };
    case 'MEDIUM':
      return { color: '#FF9800', icon: 'minus' };
    case 'HIGH':
      return { color: '#F44336', icon: 'chevron-up' };
    case 'CRITICAL':
      return { color: '#D50000', icon: 'alert' };
    default:
      return { color: '#9E9E9E', icon: 'minus' };
  }
};

const TaskCard: React.FC<TaskCardProps> = ({ task, onPress }) => {
  const statusColor = getStatusColor(task.status);
  const priorityStyle = getPriorityStyle(task.priority);
  
  // Format date for display
  const formatDate = (dateString: string) => {
    const date = new Date(dateString);
    return date.toLocaleDateString();
  };

  return (
    <TouchableOpacity onPress={() => onPress(task)}>
      <Card style={styles.card}>
        <Card.Content>
          <View style={styles.header}>
            <Title style={styles.title}>{task.title}</Title>
            <Chip 
              style={[styles.statusChip, { backgroundColor: statusColor }]}
              textStyle={styles.statusChipText}
            >
              {task.status}
            </Chip>
          </View>
          
          <Paragraph style={styles.description} numberOfLines={2}>
            {task.description || 'No description'}
          </Paragraph>
          
          <View style={styles.metaContainer}>
            <View style={styles.metaItem}>
              <Text style={styles.metaLabel}>Due:</Text>
              <Text style={styles.metaValue}>{formatDate(task.dueDate)}</Text>
            </View>
            
            <View style={styles.metaItem}>
              <Text style={styles.metaLabel}>Est. Hours:</Text>
              <Text style={styles.metaValue}>{task.estimatedHours || 'N/A'}</Text>
            </View>
            
            <View style={styles.priorityContainer}>
              <Avatar.Icon 
                size={24} 
                icon={priorityStyle.icon}
                style={{ backgroundColor: priorityStyle.color }}
              />
              <Text style={[styles.priorityText, { color: priorityStyle.color }]}>
                {task.priority}
              </Text>
            </View>
          </View>
          
          {task.assignee && (
            <View style={styles.assigneeContainer}>
              <Avatar.Text 
                size={24} 
                label={`${task.assignee.firstName[0]}${task.assignee.lastName[0]}`} 
              />
              <Text style={styles.assigneeName}>
                {`${task.assignee.firstName} ${task.assignee.lastName}`}
              </Text>
            </View>
          )}
        </Card.Content>
      </Card>
    </TouchableOpacity>
  );
};

const styles = StyleSheet.create({
  card: {
    marginBottom: 10,
    borderRadius: 8,
    elevation: 2,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 8,
  },
  title: {
    flex: 1,
    marginRight: 8,
    fontSize: 16,
    fontWeight: 'bold',
  },
  statusChip: {
    borderRadius: 12,
    height: 24,
    alignItems: 'center',
    justifyContent: 'center',
  },
  statusChipText: {
    color: 'white',
    fontSize: 12,
    fontWeight: 'bold',
  },
  description: {
    fontSize: 14,
    color: '#757575',
    marginBottom: 8,
  },
  metaContainer: {
    flexDirection: 'row',
    marginBottom: 8,
    flexWrap: 'wrap',
  },
  metaItem: {
    flexDirection: 'row',
    marginRight: 16,
    alignItems: 'center',
  },
  metaLabel: {
    fontSize: 12,
    color: '#757575',
    marginRight: 4,
  },
  metaValue: {
    fontSize: 12,
    fontWeight: 'bold',
  },
  priorityContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  priorityText: {
    fontSize: 12,
    fontWeight: 'bold',
    marginLeft: 4,
  },
  assigneeContainer: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  assigneeName: {
    fontSize: 12,
    marginLeft: 8,
  },
});

export default TaskCard; 