package com.sah.taskmanager.repository;

import com.sah.taskmanager.entity.Task;
import com.sah.taskmanager.entity.TaskStatus;
import com.sah.taskmanager.exception.TaskNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void saveTask_whenTaskIsValid_canRetrieveIt(){

        Task task = new Task();
        task.setTitle("Learn Spring Boot");
        task.setContent("Practice controller testing");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority((short) 3);

        Task savedTask = taskRepository.save(task);

        var result = taskRepository.findById(savedTask.getId());
        assertTrue(result.isPresent());
        Task retrievedTask = result.orElseThrow(()-> new TaskNotFoundException(savedTask.getId()));

        assertNotNull(savedTask.getId());
        assertEquals(task.getTitle(), retrievedTask.getTitle());
        assertEquals(task.getContent(), retrievedTask.getContent());
        assertEquals(task.getPriority(), retrievedTask.getPriority());
        assertEquals(task.getStatus(), retrievedTask.getStatus());
    }

    @Test
    void createdAtAndUpdatedAt_areInitializedWhenTaskIsCreated(){

        Task task = new Task();
        task.setTitle("Learn Spring Boot");
        task.setContent("Practice controller testing");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority((short) 3);

        Task savedTask = taskRepository.saveAndFlush(task);
        Instant originalUpdateAT = savedTask.getUpdatedAt();
        var result = taskRepository.findById(savedTask.getId());
        assertTrue(result.isPresent());
        Task originalTask = result.orElseThrow(()-> new TaskNotFoundException(savedTask.getId()));
        originalTask.setTitle("Updated Task");
        Task updatedTask = taskRepository.saveAndFlush(originalTask);


        assertNotNull(savedTask.getCreatedAt());
        assertNotNull(savedTask.getUpdatedAt());
        assertTrue(updatedTask.getUpdatedAt().isAfter(originalUpdateAT)|| updatedTask.getUpdatedAt().equals(originalUpdateAT));
    }


    @Test
    void findById_whenTaskDoesNotExist_returnsEmptyOptional(){
        Optional<Task> result = taskRepository.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void saveTask_whenTitleIsBlank_throwsException(){
        Task task = new Task();
        task.setTitle("  ");
        task.setContent("Practice controller testing");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority((short) 3);


        assertThrows(ConstraintViolationException.class, ()->{
            taskRepository.saveAndFlush(task);
        });
    }


    @Test
    void saveTask_whenTitleIsNull_throwsException(){

        assertThrows(DataIntegrityViolationException.class, ()->{
            jdbcTemplate.execute("INSERT INTO tasks(title, priority, status) VALUES(NULL, 3, 'todo');");
        });
    }
}

