package ru.home.mywizard_bot.botapi.handlers.fillingprofile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.home.mywizard_bot.scenario.Enemy;

/**
 * Данные анкеты пользователя
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData {
    String name;
    String gender;
    String color;
    String movie;
    String song;
    int age;
    int number;

    int currentParagraph = 1;
    int enemyStrength;
    int Strength = 10;
    int dexterity = 12;
    int damage = 2;
    Enemy enemy;

    public void setEnemy(Enemy enemy) {
        this.enemy = new Enemy(enemy.getName(), enemy.getId(), enemy.getDexterity(), enemy.getStrength(), enemy.getIntelligence());
    }
}
