package org.example.scaleaidemo.service;

import org.example.scaleaidemo.model.Task;
import org.example.scaleaidemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ClassificationService {

    @Autowired
    private LlmService llmService;

    @Autowired
    private JsonService jsonService;

    private final List<String> TASK_CATEGORIES = Arrays.asList(
        "Bug Fix", "Feature Request", "Documentation", "Performance", "Security", "Maintenance"
    );

    private final List<String> USER_DEPARTMENT_CATEGORIES = Arrays.asList(
        "Engineering", "Marketing", "Sales", "HR", "Finance", "Operations", "Support"
    );

    public int classifyTasks(String jsonFilePath, String columnToClassify, String outputFilePath) throws Exception {
        List<Task> tasks = jsonService.readListFromJsonFile(jsonFilePath, Task.class);
        int processedCount = 0;

        for (Task task : tasks) {
            String textToClassify = getTaskColumnValue(task, columnToClassify);
            if (textToClassify != null && !textToClassify.isEmpty()) {
                String classification = llmService.classifyText(textToClassify, columnToClassify, TASK_CATEGORIES);
                task.setCategory(classification);
                processedCount++;
            }
        }

        jsonService.writeListToJsonFile(tasks, outputFilePath);
        return processedCount;
    }

    public int classifyUsers(String jsonFilePath, String columnToClassify, String outputFilePath) throws Exception {
        List<User> users = jsonService.readListFromJsonFile(jsonFilePath, User.class);
        int processedCount = 0;

        for (User user : users) {
            String textToClassify = getUserColumnValue(user, columnToClassify);
            if (textToClassify != null && !textToClassify.isEmpty()) {
                String classification = llmService.classifyText(textToClassify, columnToClassify, USER_DEPARTMENT_CATEGORIES);
                user.setClassification(classification);
                processedCount++;
            }
        }

        jsonService.writeListToJsonFile(users, outputFilePath);
        return processedCount;
    }

    public int classifyTasksWithCustomCategories(String jsonFilePath, String columnToClassify,
                                               List<String> categories, String outputFilePath) throws Exception {
        List<Task> tasks = jsonService.readListFromJsonFile(jsonFilePath, Task.class);
        int processedCount = 0;

        for (Task task : tasks) {
            String textToClassify = getTaskColumnValue(task, columnToClassify);
            if (textToClassify != null && !textToClassify.isEmpty()) {
                String classification = llmService.classifyText(textToClassify, columnToClassify, categories);
                task.setCategory(classification);
                processedCount++;
            }
        }

        jsonService.writeListToJsonFile(tasks, outputFilePath);
        return processedCount;
    }

    private String getTaskColumnValue(Task task, String column) {
        switch (column.toLowerCase()) {
            case "title":
                return task.getTitle();
            case "description":
                return task.getDescription();
            case "status":
                return task.getStatus();
            case "priority":
                return task.getPriority();
            case "assignee":
                return task.getAssignee();
            default:
                return task.getDescription(); // Default to description
        }
    }

    private String getUserColumnValue(User user, String column) {
        switch (column.toLowerCase()) {
            case "name":
                return user.getName();
            case "email":
                return user.getEmail();
            case "department":
                return user.getDepartment();
            case "role":
                return user.getRole();
            default:
                return user.getRole(); // Default to role
        }
    }
}