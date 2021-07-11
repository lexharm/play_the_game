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
import ru.home.mywizard_bot.scenario.checks.Check;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Данные анкеты пользователя
 */

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData {
    boolean activeGame;
    BotState botState;
    Paragraph currentMenu;
    Paragraph currentParagraph = new Paragraph("dummy", "dummy");
    int enemyStrength;
    int strength;
    int dexterity;
    int damage;
    Enemy enemy;
    Map<String, Integer> inventory = new HashMap<>();
    Map<String, Integer> checks = new HashMap<>();
    List<Check> combatChecks = new ArrayList<>();
    String message = "";

    public void setEnemy(Enemy enemy) {
        this.enemy = new Enemy(enemy.getName(), enemy.getId(), enemy.getDexterity(), enemy.getStrength(), enemy.getIntelligence());
    }

    public void clearEnemy() {
        this.enemy = new Enemy("dummy", "dummy", 0, 0, 0);
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
        return "UserProfileData{" + "\n" +
                "Сила = " + strength + "\n" +
                "Ловкость = " + dexterity + "\n" +
                "Наносимый урон = " + damage + "\n" +
                "Предметы=" + inventory + "\n" +
                "Проверки=" + checks +
                '}';
    }

    public String getCombatInfo() {
        return "Ваша сила=" + strength +
                ", ловкость=" + dexterity;
    }

    public Paragraph getCurrentMenu(Story story) {
        if (currentMenu == null)
            currentMenu = story.getInitialMenuParagraph();
        return currentMenu;
    }
}
