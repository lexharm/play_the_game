package ru.home.mywizard_bot.scenario;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.actions.InlineLink;
import ru.home.mywizard_bot.scenario.features.CombatBlock;
import ru.home.mywizard_bot.scenario.features.HitEnemy;
import ru.home.mywizard_bot.utils.Emojis;

public class Combat {
    public static void newTurn(UserProfileData profileData) {
        StringBuilder sb = new StringBuilder();
        sb.append("*Ход ").append(profileData.getCombatTurn()).append("*\n");
        profileData.getEnemies().forEach(x -> sb.append(x.toString(profileData)));
        boolean classicCombat = profileData.getEnemies().get(0).getInitDexterity() != 0;
        sb.append(profileData.getCombatInfo(classicCombat));
        Paragraph paragraph = profileData.getCurrentCombatTurn();
        paragraph.addText(sb.toString());
        profileData.getEnemies().stream().filter(x -> x.getStrength() > 0)
                .forEach(x -> paragraph.addAction(new InlineLink(Emojis.SWORDS + " Атаковать " + x.getCaption(), x.getId(), new HitEnemy(x.getId()))));
        paragraph.addAction(new InlineLink(Emojis.SHIELD + " Защищаться", "block", new CombatBlock()));
    }

    public static void combatEnd(UserProfileData profileData) {
        Paragraph paragraph = profileData.getCurrentCombatTurn();
        paragraph.addText("*Поединок закончился.*");
        profileData.getCurrentParagraph().getMovementLinks().forEach(paragraph::addAction);
        profileData.setCombatTurn(1);
    }
}
