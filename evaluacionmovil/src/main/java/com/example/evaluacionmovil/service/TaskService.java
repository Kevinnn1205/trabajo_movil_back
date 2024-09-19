package com.example.evaluacionmovil.service;

import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.evaluacionmovil.model.Task;
import com.example.evaluacionmovil.model.TaskStatus;
import com.example.evaluacionmovil.repository.TaskRepository;
import java.time.LocalDate;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        actualizarEstado(task);
        return taskRepository.save(task);
    }

    public Optional<Task> updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(existingTask -> {
            existingTask.setTitle(updatedTask.getTitle());
            existingTask.setDueDate(updatedTask.getDueDate());
            existingTask.setAssignedTo(updatedTask.getAssignedTo());
            existingTask.setStatus(updatedTask.getStatus());
            actualizarEstado(existingTask);
            return taskRepository.save(existingTask);
        });
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    private void actualizarEstado(Task task) {
        if (task.getDueDate().isBefore(LocalDate.now()) && task.getStatus() == TaskStatus.PENDIENTE) {
            task.setStatus(TaskStatus.VENCIDA);
        }
    }
}
