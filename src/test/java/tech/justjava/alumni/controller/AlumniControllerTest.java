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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import tech.justjava.alumni.entity.DocumentRequest;
import tech.justjava.alumni.service.AlumniService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AlumniControllerTest {

    @Mock
    private AlumniService alumniService;

    @Mock
    private RuntimeService runtimeService;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private AlumniController alumniController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(alumniController).build();
    }

    @Test
    public void testShowStartForm() throws Exception {
        mockMvc.perform(get("/alumni/start"))
                .andExpect(status().isOk())
                .andExpect(view().name("alumni/start"))
                .andExpect(model().attributeExists("documentRequest"));
    }

    @Test
    public void testStartProcess() throws Exception {
        DocumentRequest documentRequest = new DocumentRequest();
        documentRequest.setDocumentType("Transcript");
        documentRequest.setPaymentGateway("Remita");
        documentRequest.setAlumniId("12345");

        mockMvc.perform(post("/alumni/start")
                .flashAttr("documentRequest", documentRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alumni/tasks"));

        verify(alumniService, times(1)).startProcess(eq("alumniProj"), any(Map.class));
    }

    @Test
    public void testGetTasks() throws Exception {
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(alumniService.getTasks()).thenReturn(tasks);

        mockMvc.perform(get("/alumni/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("alumni/tasks"))
                .andExpect(model().attribute("tasks", tasks));
    }

    @Test
    public void testShowTaskForm() throws Exception {
        Task task = mock(Task.class);
        when(task.getTaskDefinitionKey()).thenReturn("submitRequest");
        when(taskService.createTaskQuery().taskId("1")).thenReturn(mock(org.flowable.task.api.TaskQuery.class));
        when(taskService.createTaskQuery().taskId("1").singleResult()).thenReturn(task);

        mockMvc.perform(get("/alumni/task/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("alumni/submit-request"))
                .andExpect(model().attribute("task", task));
    }

    @Test
    public void testCompleteTask() throws Exception {
        mockMvc.perform(post("/alumni/task/1")
                .param("documentType", "Transcript")
                .param("paymentGateway", "Remita")
                .param("alumniId", "12345"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/alumni/tasks"));

        verify(alumniService, times(1)).completeTask(eq("1"), any(Map.class));
    }
}
