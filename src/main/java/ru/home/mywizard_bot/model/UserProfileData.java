package ru.home.mywizard_bot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.*;
import ru.home.mywizard_bot.scenario.checks.Check;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Данные анкеты пользователя
 */

@Data
@Document(collection = "userProfileData")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData implements Serializable {
    @Id
    String id;
    long chatId;
    boolean activeGame;
    BotState botState;
    Paragraph currentMenu;
    Paragraph currentParagraph = new Paragraph("dummy", "dummy");
    int enemyStrength;
    int strength;
    int initStrength;
    int dexterity;
    int initDexterity;
    int thoughtPower;
    int initThoughtPower;
    int attackPower;
    int damage;
    Enemy enemy;
    List<ru.home.mywizard_bot.scenario.actions.Enemy> enemies = new ArrayList<>();
    int combatTurn = 0;
    Map<String, Integer> inventory = new HashMap<>();
    Map<String, Integer> checks = new HashMap<>();
    List<Check> combatChecks = new ArrayList<>();
    String message = "";
    Integer lastMessageId;
    boolean hasReplyKeyboard = false;
    boolean hasInlineKeyboard = false;

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

    public String getCombatInfo(boolean classicCombat) {
        StringBuilder sb = new StringBuilder();
        sb.append("ИГРОК").append("\n");
        sb.append("Здоровье ").append(strength).append("/").append(initStrength).append("\n");
        sb.append(classicCombat ? "Ловкость " : " Сила мысли ").append(classicCombat ? dexterity : thoughtPower).append("\n");
        int dice = Dice.roll();
        attackPower = dice * 2 + (classicCombat ? dexterity : thoughtPower);
        sb.append("Мощность атаки ").append("(").append(dice).append(" x 2) + ")
                .append(classicCombat ? dexterity : thoughtPower).append(" = ").append(attackPower).append("\n").append("\n");
        return sb.toString();
    }

    public Paragraph getCurrentMenu(Story story) {
        if (currentMenu == null)
            currentMenu = story.getInitialMenuParagraph();
        return currentMenu;
    }

    public int getCombatTurn() {
        return ++combatTurn;
    }
}
