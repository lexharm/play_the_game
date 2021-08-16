package ru.home.mywizard_bot.scenario.actions;

import lombok.Data;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Dice;
import ru.home.mywizard_bot.utils.Emojis;

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

    public String toString(UserProfileData profileData) {
        boolean classicCombat = dexterity != 0;
        StringBuilder sb = new StringBuilder();
        //TODO: Хз как добиться идеального выравнивания, поэтому пока простая реализация
        sb.append("*").append(caption.toUpperCase()).append("*").append("\n");
        /*String paramTitle = "Здоровье ";
        String params;
        sb.append("`").append(paramTitle).append("|");
        String paramValue = strength + "/" + initStrength;
        int gapLength = (paramTitle.length() - paramValue.length()) / 2;
        params = Strings.repeat(" ", gapLength) + paramValue;
        gapLength = paramTitle.length() - params.length();
        params += Strings.repeat(" ", gapLength) + "|";
        paramTitle = (dexterity != 0) ? " Ловкость" : " Сила мысли";
        sb.append(paramTitle).append("\n");
        paramValue = (dexterity != 0) ? dexterity.toString() : thoughtPower.toString();
        gapLength = (paramTitle.length() - paramValue.length()) / 2;
        params += Strings.repeat(" ", gapLength) + paramValue;
        sb.append(params).append("`\n");*/
        sb.append("Здоровье ").append(hide(profileData, strength.toString() + "/" + initStrength.toString(), dexterity != 0 ? 4 : 5))
                .append("\n").append((dexterity != 0) ? "Ловкость " : "Сила мысли ")
                .append((dexterity != 0) ? hide(profileData, dexterity.toString(), 8) : hide(profileData, thoughtPower.toString(), 9))
                .append("\n");
        int dice = Dice.roll();
        attackPower = dice * 2 + (dexterity != 0 ? dexterity : thoughtPower);
        StringBuilder attackPowerSB = new StringBuilder();
        for (int i = 0; i < profileData.getCombatPowerRange().size() - 1; i++) {
            float limit = profileData.getCombatPowerRange().get(i);
            if (attackPower >= limit) {
                attackPowerSB.append(classicCombat ? Emojis.DAGGER : Emojis.SPARKLES);
            }
        }
        sb.append("Мощность атаки ").append(attackPowerSB).append(hide(profileData, attackPower.toString(), dexterity != 0 ? 6 : 7)).append("\n").append("\n");
        return sb.toString();
    }

    public static String hide(UserProfileData profileData, String s, int level) {
        int thoughtPower = profileData.getThoughtPower();
        return thoughtPower < level ? "_???_" : s;
    }
}
