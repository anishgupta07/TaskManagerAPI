package com.example.TaskManager.service;

import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.entity.User;
import com.example.TaskManager.repository.TaskRepository;
import com.example.TaskManager.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService{
    @Autowired
    private TaskRepository repo;
    
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ModelMapper modelMapper;
    
    private User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Task createTask(TaskDTO dto) {

        Task task = new Task();

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueTime(dto.getDueTime());
        task.setStatus("PENDING");
        task.setUser(getCurrentUser());

        return repo.save(task);
    }

    @Override
    public List<Task> getAllTasks(){
        return repo.findByUser(getCurrentUser());
    }

    @Override
    public Task updateTask(Long id , TaskDTO dto){
        Task task = repo.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new RuntimeException("Task not found or does not belong to user"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());

        return repo.save(task);
    }

    @Override
    public void deleteTask(Long id){
        Task task = repo.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new RuntimeException("Task not found or does not belong to user"));
        repo.delete(task);
    }
}
