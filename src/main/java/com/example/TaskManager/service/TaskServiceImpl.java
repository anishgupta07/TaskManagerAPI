package com.example.TaskManager.service;

import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.repository.TaskRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository repo;

    @Autowired
    private ModelMapper modelMapper;

    public Task createTask(TaskDTO dto) {

        Task task = new Task();

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueTime(dto.getDueTime());
        task.setStatus("PENDING");

        return repo.save(task);
    }

    @Override
    public List<Task> getAllTasks(){
        return repo.findAll();
    }

    @Override
    public Task updateTask(Long id , TaskDTO dto){
        Task task = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());

        return repo.save(task);
    }

    @Override
    public void deleteTask(Long id){
        repo.deleteById(id);
    }
}
