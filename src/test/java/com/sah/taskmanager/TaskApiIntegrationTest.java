package com.sah.taskmanager;


import com.jayway.jsonpath.JsonPath;
import com.sah.taskmanager.entity.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createTask_whenRequestIsValid_returnsCreated() throws Exception {

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "Learn Integration Testing",
                                "content": "Test the complete application",
                                "status": "IN_PROGRESS",
                                "priority": 3
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Learn Integration Testing"))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void createTask_thenRetrieveTask_returnsSavedTask() throws Exception {

        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "Learn Integration Testing",
                                "content": "Test the complete application",
                                "status": "IN_PROGRESS",
                                "priority": 3
                            }
                            """))
                .andExpect(status().isCreated())
                .andReturn();

                String responseString = result.getResponse().getContentAsString();
                Number tempId = JsonPath.read(responseString, "$.id");
                Long id = tempId.longValue();
                mockMvc.perform(get("/api/tasks/"+id))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(id))
                        .andExpect(jsonPath("$.title").value("Learn Integration Testing"))
                        .andExpect(jsonPath("$.content").value("Test the complete application"))
                        .andExpect(jsonPath("$.status").value(TaskStatus.IN_PROGRESS.name()))
                        .andExpect(jsonPath("$.priority").value(3));

    }

    @Test
    void getTask_whenTaskDoesNotExist_returnsNotFound() throws Exception{
        mockMvc.perform(get("/api/tasks/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTask_whenRequestIsInvalid_returnsBadRequest() throws Exception {

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "  ",
                                "content": "Test the complete application",
                                "status": "IN_PROGRESS",
                                "priority": 3
                            }
                            """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask_whenRequestIsValid_returnsUpdatedTask() throws Exception{

        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "Learn Integration Testing",
                                "content": "Test the complete application",
                                "status": "IN_PROGRESS",
                                "priority": 3
                            }
                            """))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = result.getResponse().getContentAsString();
        Number tempId = JsonPath.read(responseString, "$.id");
        Long id = tempId.longValue();

        mockMvc.perform(put("/api/tasks/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "title": "Master Spring Boot Testing"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath(("$.title")).value("Master Spring Boot Testing"));


        mockMvc.perform(get("/api/tasks/"+id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath(("$.title")).value("Master Spring Boot Testing"));
    }

    @Test
    void deleteTask_whenTaskExists_returnsNoContent() throws Exception{

        MvcResult result = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "title": "Learn Integration Testing",
                                "content": "Test the complete application",
                                "status": "IN_PROGRESS",
                                "priority": 3
                            }
                            """))
                .andExpect(status().isCreated())
                .andReturn();
        String responseString = result.getResponse().getContentAsString();
        Number tempId = JsonPath.read(responseString, "$.id");
        Long id = tempId.longValue();

        mockMvc.perform(delete("/api/tasks/"+id)).andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/"+id))
                .andExpect(status().isNotFound());
    }
}
