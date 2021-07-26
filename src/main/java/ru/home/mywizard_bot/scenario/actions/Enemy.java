package ru.home.mywizard_bot.scenario.actions;

import lombok.Data;

@Data
public class Enemy extends Action {
    private Integer strength;
    private Integer dexterity;
    private Integer thoughtPower;

    public Enemy() {
    }

    public Enemy(String caption, String id, Integer strength, Integer dexterity, Integer thoughtPower) {
        super(caption, id);
        this.strength = strength;
        this.dexterity = dexterity;
        this.thoughtPower = thoughtPower;
    }
}
