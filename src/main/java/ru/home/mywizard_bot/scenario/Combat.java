package ru.home.mywizard_bot.scenario;

import ru.home.mywizard_bot.model.UserProfileData;

public class Combat {
    public static void newTurn(UserProfileData profileData) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ход ").append(profileData.getCombatTurn()).append("\n");
        profileData.getEnemies().forEach(x -> sb.append(x));
        boolean classicCombat = profileData.getEnemies().get(0).getInitDexterity() != 0;
        sb.append(profileData.getCombatInfo(classicCombat));
        profileData.getCurrentParagraph().addText(sb.toString());
    }
}
