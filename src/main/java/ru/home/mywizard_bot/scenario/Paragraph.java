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
    private String imagePath;
    private boolean combat;
    private Enemy enemy;

    public Paragraph(int id, String text) {
        this.id = id;
        this.text = text;
        combat = false;
    }

    public Paragraph(int id, String text, boolean isCombat, Enemy enemy) {
        this.id = id;
        this.text = text;
        this.combat = isCombat;
        this.enemy = enemy;
    }
}
