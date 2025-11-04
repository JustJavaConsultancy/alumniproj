package tech.justjava.alumni.controller;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tech.justjava.alumni.entity.DocumentRequest;
import tech.justjava.alumni.service.AlumniProcessService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alumni")
public class AlumniProcessController {

    @Autowired
    private AlumniProcessService alumniProcessService;

    @PostMapping("/deploy")
    public String deployProcess() {
        alumniProcessService.deployProcess();
        return "Process deployed successfully";
    }

    @PostMapping("/start")
    public String startProcess(@RequestBody DocumentRequest documentRequest) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("documentType", documentRequest.getDocumentType());
        variables.put("paymentGateway", documentRequest.getPaymentGateway());
        variables.put("alumniId", documentRequest.getAlumniId());

        String processInstanceId = alumniProcessService.startProcess(variables);
        return "Process started successfully. Process Instance ID: " + processInstanceId;
    }

    @GetMapping("/tasks/{alumniId}")
    public List<Task> getTasks(@PathVariable String alumniId) {
        return alumniProcessService.getTasks(alumniId);
    }

    @PostMapping("/complete/{taskId}")
    public String completeTask(@PathVariable String taskId, @RequestBody Map<String, Object> variables) {
        alumniProcessService.completeTask(taskId, variables);
        return "Task completed successfully";
    }
}
