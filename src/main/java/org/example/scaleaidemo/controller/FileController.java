package org.example.scaleaidemo.controller;

import org.example.scaleaidemo.model.Task;
import org.example.scaleaidemo.model.User;
import org.example.scaleaidemo.service.CsvService;
import org.example.scaleaidemo.service.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private CsvService csvService;

    @Autowired
    private JsonService jsonService;

    @PostMapping("/tasks/csv-to-json")
    public ResponseEntity<Map<String, String>> convertTasksCsvToJson(
            @RequestParam String csvFilePath,
            @RequestParam String jsonFilePath) {
        try {
            List<Task> tasks = csvService.readTasksFromCsv(csvFilePath);
            jsonService.writeListToJsonFile(tasks, jsonFilePath);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Tasks CSV converted to JSON successfully");
            response.put("recordsProcessed", String.valueOf(tasks.size()));
            response.put("outputFile", jsonFilePath);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/users/csv-to-json")
    public ResponseEntity<Map<String, String>> convertUsersCsvToJson(
            @RequestParam String csvFilePath,
            @RequestParam String jsonFilePath) {
        try {
            List<User> users = csvService.readUsersFromCsv(csvFilePath);
            jsonService.writeListToJsonFile(users, jsonFilePath);

            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Users CSV converted to JSON successfully");
            response.put("recordsProcessed", String.valueOf(users.size()));
            response.put("outputFile", jsonFilePath);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/tasks/json")
    public ResponseEntity<List<Task>> getTasksFromJson(@RequestParam String jsonFilePath) {
        try {
            List<Task> tasks = jsonService.readListFromJsonFile(jsonFilePath, Task.class);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/users/json")
    public ResponseEntity<List<User>> getUsersFromJson(@RequestParam String jsonFilePath) {
        try {
            List<User> users = jsonService.readListFromJsonFile(jsonFilePath, User.class);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}