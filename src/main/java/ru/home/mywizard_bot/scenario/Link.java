package ru.home.mywizard_bot.scenario;

import lombok.*;

@Data
public class Link {
    String text;
    int id;
    Paragraph paragraph;

    public Link(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public Paragraph getParagraph() {
        if (paragraph == null) {
            paragraph = Story.getInstance().getParagraph(id);
        }
        return paragraph;
    }
}
