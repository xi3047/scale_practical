package org.example.scaleaidemo.controller;

import org.example.scaleaidemo.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classification")
public class ClassificationController {

    @Autowired
    private ClassificationService classificationService;

    @PostMapping("/tasks")
    public ResponseEntity<Map<String, String>> classifyTasks(
            @RequestParam String jsonFilePath,
            @RequestParam String columnToClassify,
            @RequestParam String outputFilePath) {
        try {
            int processedCount = classificationService.classifyTasks(jsonFilePath, columnToClassify, outputFilePath);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Tasks classified successfully");
            response.put("recordsProcessed", String.valueOf(processedCount));
            response.put("columnClassified", columnToClassify);
            response.put("outputFile", outputFilePath);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> classifyUsers(
            @RequestParam String jsonFilePath,
            @RequestParam String columnToClassify,
            @RequestParam String outputFilePath) {
        try {
            int processedCount = classificationService.classifyUsers(jsonFilePath, columnToClassify, outputFilePath);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Users classified successfully");
            response.put("recordsProcessed", String.valueOf(processedCount));
            response.put("columnClassified", columnToClassify);
            response.put("outputFile", outputFilePath);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/tasks/custom")
    public ResponseEntity<Map<String, String>> classifyTasksWithCustomCategories(
            @RequestParam String jsonFilePath,
            @RequestParam String columnToClassify,
            @RequestBody List<String> categories,
            @RequestParam String outputFilePath) {
        try {
            int processedCount = classificationService.classifyTasksWithCustomCategories(
                jsonFilePath, columnToClassify, categories, outputFilePath);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Tasks classified with custom categories successfully");
            response.put("recordsProcessed", String.valueOf(processedCount));
            response.put("columnClassified", columnToClassify);
            response.put("categoriesUsed", String.join(", ", categories));
            response.put("outputFile", outputFilePath);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/categories/tasks")
    public ResponseEntity<Map<String, Object>> getTaskCategories() {
        Map<String, Object> response = new HashMap<>();
        response.put("categories", List.of("Bug Fix", "Feature Request", "Documentation", "Performance", "Security", "Maintenance"));
        response.put("supportedColumns", List.of("title", "description", "status", "priority", "assignee"));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/categories/users")
    public ResponseEntity<Map<String, Object>> getUserCategories() {
        Map<String, Object> response = new HashMap<>();
        response.put("categories", List.of("Engineering", "Marketing", "Sales", "HR", "Finance", "Operations", "Support"));
        response.put("supportedColumns", List.of("name", "email", "department", "role"));
        return ResponseEntity.ok(response);
    }
}