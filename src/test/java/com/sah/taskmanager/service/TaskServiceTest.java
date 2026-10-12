package com.sah.taskmanager.service;

import com.sah.taskmanager.dto.CreateTaskRequest;
import com.sah.taskmanager.entity.Task;
import com.sah.taskmanager.entity.TaskStatus;
import com.sah.taskmanager.exception.TaskNotFoundException;
import com.sah.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Captor
    ArgumentCaptor<Task> taskCaptor;

    @Test
    void getTaskById_whenTaskExists_returnsTask(){
        //create a task
        Task task = new Task();
        task.setId(1L);
        task.setPriority((short) 3);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setTitle("Test get Task by Id");
        task.setContent("test a service function");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task savedTask = taskService.getTaskById(1L);

        assertEquals("Test get Task by Id", savedTask.getTitle());
        assertEquals(1L, savedTask.getId());
    }

    @Test
    void getTaskById_whenTaskDoesNotExist_throwsException(){
            when(taskRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(TaskNotFoundException.class,
                    () -> taskService.getTaskById(99L));
    }

    @Test
    void createTask_whenValidRequest_savesTask(){

        String content = "creating task for testing service ";
        String title = "New Created Task";
        TaskStatus status = TaskStatus.IN_PROGRESS;
        short priority = (short)3;

        CreateTaskRequest request = new CreateTaskRequest(title, content, status, priority, null);

        when(taskRepository.save(any(Task.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Task savedTask = taskService.createTask(request);

        verify(taskRepository).save(taskCaptor.capture());

        Task capturedTask = taskCaptor.getValue();


        assertEquals(content, capturedTask.getContent());
        assertEquals(title, capturedTask.getTitle());
        assertEquals(priority, capturedTask.getPriority());
        assertEquals(status, capturedTask.getStatus());

        assertSame(savedTask, capturedTask);
    }

    @Test
    void updateTask_whenTaskExists_updatesTaskFields(){
        Task task = new Task();
        task.setId(1L);
        task.setPriority((short) 3);
        task.setStatus(TaskStatus.TODO);
        task.setTitle("Test update Task by Id");
        task.setContent("test a service function");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Task newTask = new Task();
        newTask.setContent("Updated Content");
        newTask.setTitle("Updated Task");
        newTask.setStatus(TaskStatus.IN_PROGRESS);
        newTask.setPriority((short)5);

        Task updatedTask = taskService.updateTask(1L, newTask);

        verify(taskRepository).save(taskCaptor.capture());
        Task capturedTask = taskCaptor.getValue();

        assertEquals(1L, updatedTask.getId());

        assertEquals("Updated Task", updatedTask.getTitle());
        assertEquals("Updated Content", updatedTask.getContent());
        assertEquals(TaskStatus.IN_PROGRESS, updatedTask.getStatus());
        assertEquals((short) 5, updatedTask.getPriority());

        assertSame(capturedTask, updatedTask);
    }

    @Test
    void updateTask_whenTaskDoesNotExist_throwsException(){
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        Task newTask = new Task();
        newTask.setContent("Updated Content");
        newTask.setTitle("Updated Task");
        newTask.setStatus(TaskStatus.IN_PROGRESS);
        newTask.setPriority((short)5);

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(99L, newTask));

        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_whenTaskExists_deletesTask(){
        Task task = new Task();
        task.setId(1L);
        task.setPriority((short) 3);
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setTitle("Test Delete Task by Id");
        task.setContent("test a service function");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        taskService.deleteTask(1L);
        verify(taskRepository).delete(task);
    }

    @Test
    void deleteTask_whenTaskDoesNotExist_throwsException(){
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(TaskNotFoundException.class, ()-> taskService.deleteTask(99L));
        verify(taskRepository, never()).delete(any(Task.class));
    }

}
