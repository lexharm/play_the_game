package ru.home.mywizard_bot.scenario;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class Link {
    String text;
    int id;

    public Link(String text, int id) {
        this.text = text;
        this.id = id;
    }
}
