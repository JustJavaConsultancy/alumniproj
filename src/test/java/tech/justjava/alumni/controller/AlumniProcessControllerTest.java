package tech.justjava.alumni.controller;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import tech.justjava.alumni.entity.DocumentRequest;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class AlumniProcessControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AlumniProcessService alumniProcessService;

    @InjectMocks
    private AlumniProcessController alumniProcessController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(alumniProcessController).build();
    }

    @Test
    public void testDeployProcess() throws Exception {
        doNothing().when(alumniProcessService).deployProcess();

        mockMvc.perform(post("/api/alumni/deploy"))
                .andExpect(status().isOk())
                .andExpect(content().string("Process deployed successfully"));

        verify(alumniProcessService, times(1)).deployProcess();
    }

    @Test
    public void testStartProcess() throws Exception {
        DocumentRequest documentRequest = new DocumentRequest();
        documentRequest.setDocumentType("Transcript");
        documentRequest.setPaymentGateway("Remita");
        documentRequest.setAlumniId("12345");

        when(alumniProcessService.startProcess(anyMap())).thenReturn("processInstanceId");

        mockMvc.perform(post("/api/alumni/start")
                .contentType("application/json")
                .content("{\"documentType\":\"Transcript\",\"paymentGateway\":\"Remita\",\"alumniId\":\"12345\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Process started successfully. Process Instance ID: processInstanceId"));

        verify(alumniProcessService, times(1)).startProcess(anyMap());
    }

    @Test
    public void testGetTasks() throws Exception {
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(alumniProcessService.getTasks(anyString())).thenReturn(tasks);

        mockMvc.perform(get("/api/alumni/tasks/12345"))
                .andExpect(status().isOk());

        verify(alumniProcessService, times(1)).getTasks("12345");
    }

    @Test
    public void testCompleteTask() throws Exception {
        doNothing().when(alumniProcessService).completeTask(anyString(), anyMap());

        mockMvc.perform(post("/api/alumni/complete/task123")
                .contentType("application/json")
                .content("{\"approval\":\"approved\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task completed successfully"));

        verify(alumniProcessService, times(1)).completeTask("task123", anyMap());
    }
}
