package com.example.TaskManager.service;

import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.entity.Task;

import java.util.List;

public interface TaskService {
    Task createTask(TaskDTO dto);
    List<Task> getAllTasks();
    Task updateTask(Long id, TaskDTO dto);
    void deleteTask(Long id);
}
