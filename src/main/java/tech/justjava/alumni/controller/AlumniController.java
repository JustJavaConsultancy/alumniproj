package tech.justjava.alumni.controller;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import tech.justjava.alumni.entity.DocumentRequest;
import tech.justjava.alumni.service.AlumniService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/alumni")
public class AlumniController {

    @Autowired
    private AlumniService alumniService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @GetMapping("/start")
    public String showStartForm(Model model) {
        model.addAttribute("documentRequest", new DocumentRequest());
        return "alumni/start";
    }

    @PostMapping("/start")
    public String startProcess(@ModelAttribute DocumentRequest documentRequest) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("documentType", documentRequest.getDocumentType());
        variables.put("paymentGateway", documentRequest.getPaymentGateway());
        variables.put("alumniId", documentRequest.getAlumniId());

        alumniService.startProcess("alumniProj", variables);
        return "redirect:/alumni/tasks";
    }

    @GetMapping("/tasks")
    public String getTasks(Model model) {
        List<Task> tasks = alumniService.getTasks();
        model.addAttribute("tasks", tasks);
        return "alumni/tasks";
    }

    @GetMapping("/task/{id}")
    public String showTaskForm(@PathVariable String id, Model model) {
        Task task = taskService.createTaskQuery().taskId(id).singleResult();
        model.addAttribute("task", task);

        if ("submitRequest".equals(task.getTaskDefinitionKey())) {
            model.addAttribute("documentRequest", new DocumentRequest());
            return "alumni/submit-request";
        } else if ("payTranscript".equals(task.getTaskDefinitionKey()) ||
                   "payCertificate".equals(task.getTaskDefinitionKey()) ||
                   "payLetter".equals(task.getTaskDefinitionKey())) {
            return "alumni/payment";
        } else if ("payRemita".equals(task.getTaskDefinitionKey()) ||
                   "payPaystack".equals(task.getTaskDefinitionKey()) ||
                   "payInterswitch".equals(task.getTaskDefinitionKey())) {
            return "alumni/payment-gateway";
        } else if ("approveTranscript".equals(task.getTaskDefinitionKey()) ||
                   "approveCertificate".equals(task.getTaskDefinitionKey()) ||
                   "approveLetter".equals(task.getTaskDefinitionKey())) {
            return "alumni/approval";
        }

        return "redirect:/alumni/tasks";
    }

    @PostMapping("/task/{id}")
    public String completeTask(@PathVariable String id, @RequestParam(required = false) String documentType,
                              @RequestParam(required = false) String paymentGateway,
                              @RequestParam(required = false) String alumniId,
                              @RequestParam(required = false) String approvalStatus) {
        Map<String, Object> variables = new HashMap<>();
        if (documentType != null) variables.put("documentType", documentType);
        if (paymentGateway != null) variables.put("paymentGateway", paymentGateway);
        if (alumniId != null) variables.put("alumniId", alumniId);
        if (approvalStatus != null) variables.put("approvalStatus", approvalStatus);

        alumniService.completeTask(id, variables);
        return "redirect:/alumni/tasks";
    }
}
