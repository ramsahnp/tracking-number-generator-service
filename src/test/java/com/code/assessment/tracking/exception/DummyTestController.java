package com.code.assessment.tracking.exception;

import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Validated
public class DummyTestController {

    @GetMapping
    public String validateCode(@RequestParam @Pattern(regexp = "^[A-Z]{2}$") String code) {
        return "Valid code";
    }

    @GetMapping("/test-error")
    public String throwError() {
        throw new RuntimeException("Simulated exception");
    }
}
