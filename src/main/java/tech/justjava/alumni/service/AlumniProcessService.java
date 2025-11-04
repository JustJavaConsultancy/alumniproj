package tech.justjava.alumni.service;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AlumniProcessService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    public void deployProcess() {
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/alumniProj.bpmn20.xml")
                .name("Alumni Document Request Process")
                .deploy();
    }

    public String startProcess(Map<String, Object> variables) {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("alumniProj", variables);
        return processInstance.getId();
    }

    public List<Task> getTasks(String alumniId) {
        return taskService.createTaskQuery()
                .processVariableValueEquals("alumniId", alumniId)
                .list();
    }

    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }
}
