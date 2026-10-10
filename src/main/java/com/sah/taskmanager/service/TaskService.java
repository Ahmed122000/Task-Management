package com.sah.taskmanager.service;

import com.sah.taskmanager.dto.CreateTaskRequest;
import com.sah.taskmanager.entity.Task;
import com.sah.taskmanager.entity.TaskStatus;
import com.sah.taskmanager.exception.TaskNotFoundException;
import com.sah.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }


    public Task createTask(CreateTaskRequest request){

        Task task = new Task();
        task.setDueDate(request.getDueDate());
        task.setContent(request.getContent());
        task.setTitle(request.getTitle());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.TODO);

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task updateTask(Long id, Task newTask){
        Task originalTask = getTaskById(id);

        originalTask.setContent(newTask.getContent());
        originalTask.setTitle(newTask.getTitle());
        originalTask.setStatus(newTask.getStatus());
        originalTask.setPriority(newTask.getPriority());
        originalTask.setDueDate(newTask.getDueDate());

        return taskRepository.save(originalTask);
    }

    public void deleteTask(Long id){
        Task task = getTaskById(id);
        taskRepository.delete(task);
    }
}
