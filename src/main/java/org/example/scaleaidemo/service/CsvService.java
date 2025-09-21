package org.example.scaleaidemo.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.example.scaleaidemo.model.Task;
import org.example.scaleaidemo.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvService {

    private final WebClient webClient;

    public CsvService() {
        this.webClient = WebClient.builder().build();
    }

    public List<Task> readTasksFromUrl(String csvUrl) throws IOException, CsvException {
        String csvContent = fetchCsvFromUrl(csvUrl);
        return parseTasksFromCsvContent(csvContent);
    }

    public List<User> readUsersFromUrl(String csvUrl) throws IOException, CsvException {
        String csvContent = fetchCsvFromUrl(csvUrl);
        return parseUsersFromCsvContent(csvContent);
    }

    private String fetchCsvFromUrl(String csvUrl) {
        try {
            return webClient.get()
                .uri(csvUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch CSV from URL: " + csvUrl + ". Error: " + e.getMessage());
        }
    }

    public List<Task> readTasksFromCsv(String filePath) throws IOException, CsvException {
        List<Task> tasks = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();

            if (records.isEmpty()) {
                return tasks;
            }

            // Skip header row
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                if (record.length >= 6) {
                    Task task = new Task(
                        record[0], // id
                        record[1], // title
                        record[2], // description
                        record[3], // status
                        record[4], // priority
                        record[5]  // assignee
                    );
                    tasks.add(task);
                }
            }
        }

        return tasks;
    }

    private List<Task> parseTasksFromCsvContent(String csvContent) throws IOException, CsvException {
        List<Task> tasks = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new StringReader(csvContent))) {
            List<String[]> records = reader.readAll();

            if (records.isEmpty()) {
                return tasks;
            }

            // Skip header row
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                if (record.length >= 6) {
                    Task task = new Task(
                        record[0], // id
                        record[1], // title
                        record[2], // description
                        record[3], // status
                        record[4], // priority
                        record[5]  // assignee
                    );
                    tasks.add(task);
                }
            }
        }

        return tasks;
    }

    public List<User> readUsersFromCsv(String filePath) throws IOException, CsvException {
        List<User> users = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();

            if (records.isEmpty()) {
                return users;
            }

            // Skip header row
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                if (record.length >= 6) {
                    User user = new User(
                        record[0], // id
                        record[1], // name
                        record[2], // email
                        record[3], // department
                        record[4], // role
                        Boolean.parseBoolean(record[5]) // active
                    );
                    users.add(user);
                }
            }
        }

        return users;
    }

    private List<User> parseUsersFromCsvContent(String csvContent) throws IOException, CsvException {
        List<User> users = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new StringReader(csvContent))) {
            List<String[]> records = reader.readAll();

            if (records.isEmpty()) {
                return users;
            }

            // Skip header row
            for (int i = 1; i < records.size(); i++) {
                String[] record = records.get(i);
                if (record.length >= 6) {
                    User user = new User(
                        record[0], // id
                        record[1], // name
                        record[2], // email
                        record[3], // department
                        record[4], // role
                        Boolean.parseBoolean(record[5]) // active
                    );
                    users.add(user);
                }
            }
        }

        return users;
    }
}