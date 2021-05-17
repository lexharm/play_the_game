package ru.home.mywizard_bot.scenario;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
public class Paragraph {
    int id;
    String text;
    private List<Link> links;

    public Paragraph(int id, String text) {
        this.id = id;
        this.text = text;
    }
}
