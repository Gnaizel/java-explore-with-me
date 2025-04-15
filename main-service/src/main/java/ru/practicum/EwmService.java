package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.stats_client", "ru.practicum.main_service"})
public class EwmService {
    public static void main(String[] args) {
        SpringApplication.run(EwmService.class, args);
    }
}
