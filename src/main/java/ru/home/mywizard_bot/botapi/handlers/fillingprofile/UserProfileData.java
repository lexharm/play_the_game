package ru.home.mywizard_bot.botapi.handlers.fillingprofile;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.Enemy;
import ru.home.mywizard_bot.scenario.Item;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;

import java.util.HashMap;
import java.util.Map;

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

    BotState botState;
    Paragraph currentMenu;
    Paragraph currentParagraph;
    int enemyStrength;
    int strength;
    int dexterity;
    int damage;
    Enemy enemy;
    Map<String, Integer> inventory = new HashMap<>();
    Map<String, Integer> checks = new HashMap<>();

    public void setEnemy(Enemy enemy) {
        this.enemy = new Enemy(enemy.getName(), enemy.getId(), enemy.getDexterity(), enemy.getStrength(), enemy.getIntelligence());
    }

    public void clearEnemy() {
        this.enemy = null;
    }

    public void addItem(Item item) {
        if (item.isVisible()) {
            if (inventory.containsKey(item.getId())) {
                inventory.put(item.getId(), inventory.get(item.getId()) + 1);
            } else {
                inventory.put(item.getId(), 1);
            }
        } else {
            if (checks.containsKey(item.getId())) {
                checks.put(item.getId(), checks.get(item.getId()) + 1);
            } else {
                checks.put(item.getId(), 1);
            }
        }
    }

    @Override
    public String toString() {
        return "UserProfileData{" +
                "strength=" + strength +
                ", inventory=" + inventory +
                ", checks=" + checks +
                '}';
    }

    public Paragraph getCurrentMenu(Story story) {
        if (currentMenu == null)
            currentMenu = story.getInitialMenuParagraph();
        return currentMenu;
    }
}
