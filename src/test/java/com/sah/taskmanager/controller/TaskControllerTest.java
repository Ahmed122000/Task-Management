
package com.sah.taskmanager.controller;

import com.sah.taskmanager.dto.CreateTaskRequest;
import com.sah.taskmanager.exception.TaskNotFoundException;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import tools.jackson.databind.json.JsonMapper;
import com.sah.taskmanager.entity.Task;
import com.sah.taskmanager.entity.TaskStatus;
import com.sah.taskmanager.service.TaskService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockitoBean
    private TaskService taskService;

    ArgumentCaptor<CreateTaskRequest> requestCaptor = ArgumentCaptor.forClass(CreateTaskRequest.class);

    ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

    @Test
    void getTaskById_whenTaskExists_returns200() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Learn Spring Boot");
        task.setContent("Practice controller testing");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority((short) 3);

        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Spring Boot"));
    }


    @Test
    void getTaskById_whenTaskDoesNotExist_returns404() throws Exception {

        when(taskService.getTaskById(99L)).thenThrow(new TaskNotFoundException(99L));
        mockMvc.perform(get("/api/tasks/99")).andExpect(status().isNotFound());
        verify(taskService).getTaskById(99L);
    }


    @Test
    void createTask_whenValidRequest_returns201() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Learn Spring Boot");
        task.setContent("Practice controller testing");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority((short) 3);

        CreateTaskRequest request = new CreateTaskRequest(
                task.getTitle(),
                task.getContent(),
                task.getStatus(),
                task.getPriority(),
                null);

        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
                .andExpect(jsonPath("$.content").value("Practice controller testing"))
                .andExpect(jsonPath("$.status").value(TaskStatus.IN_PROGRESS.name()))
                .andExpect(jsonPath("$.priority").value(3));

        verify(taskService).createTask(requestCaptor.capture());
        CreateTaskRequest capturedRequest = requestCaptor.getValue();

        assertEquals("Learn Spring Boot", capturedRequest.getTitle());
        assertEquals("Practice controller testing", capturedRequest.getContent());
        assertEquals(TaskStatus.IN_PROGRESS, capturedRequest.getStatus());
        assertEquals(3, capturedRequest.getPriority());
    }

    @Test
    void createTask_whenTitleIsBlank_returns400() throws Exception {

        CreateTaskRequest request = new CreateTaskRequest(
                "",
                "Practice controller testing",
                TaskStatus.IN_PROGRESS,
                (short)3,
                null);


        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(taskService);
    }

    @Test
    void updateTask_whenTaskExists_returns200() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Learn Spring Boot");
        task.setContent("Practice controller testing");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority((short) 3);

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(task);

        mockMvc.perform(put("/api/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
                .andExpect(jsonPath("$.content").value("Practice controller testing"))
                .andExpect(jsonPath("$.status").value(TaskStatus.IN_PROGRESS.name()))
                .andExpect(jsonPath("$.priority").value(3));

        verify(taskService).updateTask(eq(1L), taskCaptor.capture());
        Task capturedTask = taskCaptor.getValue();

        assertEquals(1L, capturedTask.getId());
        assertEquals(task.getTitle(), capturedTask.getTitle());
        assertEquals(task.getContent(), capturedTask.getContent());
    }


    @Test
    void updateTask_whenTaskNotExists_returns404() throws Exception {

        Task task = new Task();
        task.setId(1L);
        task.setTitle("Learn Spring Boot");
        task.setContent("Practice controller testing");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setPriority((short) 3);

        when(taskService.updateTask(eq(99L), any(Task.class)))
                .thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(put("/api/tasks/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(task)))
                .andExpect(status().isNotFound());

        verify(taskService).updateTask(eq(99L), any(Task.class));
    }

    @Test
    void deleteTask_whenTaskExists_returns204() throws Exception {

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(1L);
    }

    @Test
    void deleteTask_whenTaskNotExists_returns404() throws Exception {
        doThrow(new TaskNotFoundException(99L)).when(taskService).deleteTask(99L);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isNotFound());

        verify(taskService).deleteTask(99L);
    }
}
