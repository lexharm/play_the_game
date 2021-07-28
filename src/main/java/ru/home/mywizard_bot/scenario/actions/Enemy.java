package ru.home.mywizard_bot.scenario.actions;

import lombok.Data;
import ru.home.mywizard_bot.scenario.Dice;

@Data
public class Enemy extends Action {
    private Integer initStrength;
    private Integer initDexterity;
    private Integer initThoughtPower;
    private Integer strength;
    private Integer dexterity;
    private Integer thoughtPower;
    private Integer attackPower;
    private Integer damage = 2;

    public Enemy() {
    }

    public Enemy(String caption, String id, Integer strength, Integer dexterity, Integer thoughtPower) {
        super(caption, id);
        this.initStrength = strength;
        this.initDexterity = dexterity;
        this.initThoughtPower = thoughtPower;
        this.strength = strength;
        this.dexterity = dexterity;
        this.thoughtPower = thoughtPower;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(caption.toUpperCase()).append("\n");
        /*TODO: Шрифт телеги не строго выравнивает по знакам и следующая реализация выглядит съехавшей
        String paramTitle = "Здоровье ";
        String params;
        sb.append(paramTitle).append("|");
        String paramValue = strength + "/" + initStrength;
        int gapLength = (paramTitle.length() - paramValue.length()) / 2;
        params = Strings.repeat(" ", gapLength) + paramValue + Strings.repeat(" ", gapLength) + "|";
        paramTitle = (dexterity != 0) ? " Ловкость" : " Сила мысли";
        sb.append(paramTitle).append("\n");
        paramValue = (dexterity != 0) ? dexterity.toString() : thoughtPower.toString();
        gapLength = (paramTitle.length() - paramValue.length()) / 2;
        params += Strings.repeat(" ", gapLength) + paramValue;
        sb.append(params).append("\n");*/
        sb.append("Здоровье ").append(strength).append("/").append(initStrength).append("\n");
        sb.append((dexterity != 0) ? "Ловкость " : "Сила мысли ").append((dexterity != 0) ? dexterity : thoughtPower).append("\n");
        int dice = Dice.roll();
        attackPower = dice * 2 + (dexterity != 0 ? dexterity : thoughtPower);
        sb.append("Мощность атаки ").append("(").append(dice).append(" x 2) + ")
                .append(dexterity != 0 ? dexterity : thoughtPower).append(" = ").append(attackPower).append("\n").append("\n");
        return sb.toString();
    }

    public static void main(String[] args) {
        Enemy enemy = new Enemy("Злобный Арктус", "evilArctus", 10, 0, 12);
        System.out.println(enemy);
    }
}
