package org.example.scaleaidemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello_world")
    public Map<String, String> helloWorld() {
        Map<String, String> response = new HashMap<>();
        response.put("hello", "world");
        return response;
    }
}