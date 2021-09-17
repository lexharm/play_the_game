package ru.home.mywizard_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyWizardBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyWizardBotApplication.class, args);
    }
}
