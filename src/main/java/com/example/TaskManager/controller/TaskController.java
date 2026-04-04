package com.example.TaskManager.controller;

import com.example.TaskManager.config.ApiResponse;
import com.example.TaskManager.dto.TaskDTO;
import com.example.TaskManager.entity.Task;
import com.example.TaskManager.service.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskServiceImpl service;

    @PostMapping
    public ResponseEntity<ApiResponse<Task>> createTasks(@RequestBody TaskDTO dto) {
        Task task = service.createTask(dto);
        ApiResponse<Task> response = new ApiResponse<>();
        response.setMessage("Task created successfully");
        response.setData(task);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(){
        return ResponseEntity.ok(service.getAllTasks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id , @RequestBody TaskDTO dto){
        return ResponseEntity.ok(service.updateTask(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id){
        service.deleteTask(id);
        return ResponseEntity.ok("Task deleted Successfully");
    }
}
