package com.example.TaskManager.repository;

import com.example.TaskManager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import com.example.TaskManager.entity.User;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    List<Task> findByUser(User user);
    Optional<Task> findByIdAndUser(Long id, User user);
}
