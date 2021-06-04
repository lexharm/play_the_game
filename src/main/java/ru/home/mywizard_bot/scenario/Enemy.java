package ru.home.mywizard_bot.scenario;

import lombok.Data;

@Data
public class Enemy {
    private String name;
    private String id;
    private int dexterity;
    private int strength;
    private int intelligence;
    private int damage;

    public Enemy(String name, String id, int dexterity, int strength, int intelligence) {
        this.name = name;
        this.id = id;
        this.dexterity = dexterity;
        this.strength = strength;
        this.intelligence = intelligence;
        damage = 2;
    }

    public String toString() {
        return "Enemy{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", Ловкость=" + dexterity +
                ", strength=" + strength +
                ", intelligence=" + intelligence +
                ", damage=" + damage +
                '}';
    }

    public String getCombatInfo() {
        return name + " сила=" + strength + ", ловкость=" + dexterity;
    }
}
