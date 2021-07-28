package ru.home.mywizard_bot.scenario;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.actions.InlineLink;
import ru.home.mywizard_bot.scenario.features.CombatBlock;
import ru.home.mywizard_bot.scenario.features.HitEnemy;

public class Combat {
    public static void newTurn(UserProfileData profileData) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ход ").append(profileData.getCombatTurn()).append("\n");
        profileData.getEnemies().forEach(sb::append);
        boolean classicCombat = profileData.getEnemies().get(0).getInitDexterity() != 0;
        sb.append(profileData.getCombatInfo(classicCombat));
        Paragraph paragraph = profileData.getCurrentCombatTurn();
        paragraph.addText(sb.toString());
        profileData.getEnemies().stream().filter(x -> x.getStrength() > 0)
                .forEach(x -> paragraph.addAction(new InlineLink("Атаковать " + x.getCaption(), x.getId(), new HitEnemy(x.getId()))));
        paragraph.addAction(new InlineLink("Защищаться", "block", new CombatBlock()));
    }

    public static void combatEnd(UserProfileData profileData) {
        StringBuilder sb = new StringBuilder();
        sb.append("Ход ").append(profileData.getCombatTurn()).append("\n");
        profileData.getEnemies().forEach(sb::append);
        boolean classicCombat = profileData.getEnemies().get(0).getInitDexterity() != 0;
        sb.append(profileData.getCombatInfo(classicCombat));
        Paragraph paragraph = profileData.getCurrentCombatTurn();
        paragraph.addText(sb.toString());
        profileData.getCurrentParagraph().getMovementLinks().forEach(paragraph::addAction);
    }
}
