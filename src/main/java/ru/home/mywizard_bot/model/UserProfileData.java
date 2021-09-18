package ru.home.mywizard_bot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.Dice;
import ru.home.mywizard_bot.scenario.Item;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.scenario.checks.Check;
import ru.home.mywizard_bot.utils.Emojis;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
    String userName;
    boolean activeGame;
    BotState botState;
    Paragraph currentMenu;
    Paragraph currentParagraph = new Paragraph("dummy", "dummy");
    Paragraph currentCombatTurn;
    Paragraph newParagraph;
    int strength;
    int initStrength;
    int dexterity;
    int initDexterity;
    int thoughtPower;
    int initThoughtPower;
    int attackPower;
    int damage;
    List<ru.home.mywizard_bot.scenario.actions.Enemy> enemies = new ArrayList<>();
    int combatTurn = 1;
    String combatStatus = "";
    String additionalStatus = "";
    Map<String, Integer> inventory = new HashMap<>();
    Map<String, Integer> checks = new HashMap<>();
    List<Check> combatChecks = new ArrayList<>();
    Integer lastMessageId;
    Integer replyKeyboardHash;
    Integer inlineKeyboardHash;
    Date lastInteractionDate;
    List<Float> combatPowerRange = new ArrayList<>(7);

    public void setUserName(String userName) {
        if (userName == null || userName.isEmpty()) {
            userName = "NoNameUser";
        }
        this.userName = userName.replace("_", "").replace("*", "");
    }

    public void addAddStatus(String addition) {
        additionalStatus = addition + additionalStatus;
    }

    public void appendAddStatus(String addition) {
        additionalStatus += addition;
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

    public void removeItem(String item) {
        inventory.remove(item);
    }

    public void removeItem(Item item) {
        if (item.isVisible()) {
            if (inventory.containsKey(item.getId())) {
                inventory.put(item.getId(), inventory.get(item.getId()) - 1);
            }
        } else {
            if (checks.containsKey(item.getId())) {
                checks.put(item.getId(), checks.get(item.getId()) - 1);
            }
        }
    }

    public void removeAllItems() {
        inventory = new HashMap<>();
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

    public String showInventory() {
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(userName).append("*").append("\n");
        sb.append(String.format("*%s:* _%d/%d_\n", "Здоровье", strength, initStrength));
        sb.append(String.format("*%s:* _%d/%d_\n", "Ловкость", dexterity, initDexterity));
        sb.append(String.format("*%s:* _%d/%d_\n", "Сила мысли", thoughtPower, initThoughtPower));
        sb.append(String.format("*%s:* _%d_\n", "Наносимый урон", damage));
        sb.append("*Предметы:*\n");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            sb.append(String.format("*%s:* _%d шт._\n", entry.getKey(), entry.getValue()));
        }
        sb.append("*Проверки:*\n");
        for (Map.Entry<String, Integer> entry : checks.entrySet()) {
            sb.append(String.format("*%s:* _%d шт._\n", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }

    public String getCombatInfo(boolean classicCombat) {
        StringBuilder sb = new StringBuilder();
        sb.append("*").append(userName).append("*").append("\n");
        sb.append("Здоровье ").append(strength).append("/").append(initStrength).append("\n");
        sb.append(classicCombat ? "Ловкость " : "Сила мысли ").append(classicCombat ? dexterity : thoughtPower).append("\n");
        int dice = Dice.roll();
        attackPower = dice * 2 + (classicCombat ? dexterity : thoughtPower);
        StringBuilder attackPowerSB = new StringBuilder();
        for (int i = 0; i < combatPowerRange.size() - 1; i++) {
            float limit = combatPowerRange.get(i);
            if (attackPower >= limit) {
                attackPowerSB.append(classicCombat ? Emojis.DAGGER : Emojis.SPARKLES);
            }
        }
        sb.append("Мощность атаки ").append(attackPowerSB).append(attackPower).append("\n").append("\n");
        return sb.toString();
    }

    public Paragraph getCurrentMenu(Story story) {
        if (currentMenu == null)
            currentMenu = story.getInitialMenuParagraph();
        return currentMenu;
    }

    public int getCombatTurn() {
        return combatTurn;
    }

    public void appendCombatStatus(String s) {
        combatStatus += s;
    }

    public void incCombatTurn() {
        combatTurn++;
    }

    public void calcPowerCombatRange() {
        combatPowerRange.clear();
        boolean classicCombat = enemies.get(0).getInitDexterity() != 0;
        int maxEnemyPower = enemies.stream()
                .collect(Collectors.summarizingInt(x -> classicCombat ? x.getDexterity() : x.getThoughtPower())).getMax();
        int minEnemyPower = enemies.stream()
                .collect(Collectors.summarizingInt(x -> classicCombat ? x.getDexterity() : x.getThoughtPower())).getMin();
        Float minEdge = (float) Math.min(minEnemyPower, classicCombat ? dexterity : thoughtPower) + 2;
        Float maxEdge = (float) Math.max(maxEnemyPower, classicCombat ? dexterity : thoughtPower) + 12;
        combatPowerRange.add(minEdge);
        Float powerStep = (maxEdge - minEdge) / 6;
        for (int i = 1; i < 6; i++) {
            combatPowerRange.add(combatPowerRange.get(i - 1) + powerStep);
        }
        combatPowerRange.add(maxEdge);
        combatPowerRange.forEach(System.out::println);
    }

    public boolean isHasInlineKeyboard() {
        return inlineKeyboardHash != null;
    }

    public boolean isHasReplyKeyboard() {
        return replyKeyboardHash != null;
    }

    public boolean equalsInlineKeyboard(InlineKeyboardMarkup inlineKeyboard) {
        if (inlineKeyboard != null && inlineKeyboardHash != null) {
            return inlineKeyboard.hashCode() == inlineKeyboardHash;
        }
        return false;
    }

    public boolean equalsReplyKeyboard(ReplyKeyboardMarkup replyKeyboard) {
        if (replyKeyboard != null && replyKeyboardHash != null) {
            return replyKeyboard.hashCode() == replyKeyboardHash;
        }
        return false;
    }
}
