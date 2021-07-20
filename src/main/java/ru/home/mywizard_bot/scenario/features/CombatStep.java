package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Dice;
import ru.home.mywizard_bot.scenario.Enemy;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.scenario.checks.Check;
import ru.home.mywizard_bot.service.ApplicationContextHolder;

import java.util.ArrayList;

public class CombatStep implements Feature {
    @Override
    public void engage(UserProfileData profileData) {

        int playerStrength = profileData.getStrength();
        int playerDamage = profileData.getDamage();
        int playerDexterity = profileData.getDexterity();
        Enemy enemy = profileData.getEnemy();
        String message;

        if (enemy.getStrength() > 0 && playerStrength > 0) {
            int enemyPower = Dice.roll() * 2 + enemy.getDexterity();
            int playerPower = Dice.roll() * 2 + playerDexterity;
            if (enemyPower > playerPower) {
                playerStrength -= enemy.getDamage();
                profileData.setStrength(playerStrength);
                message = enemy.getName() + " наносит Вам урон " + enemy.getDamage() + " ед.";
            } else if (enemyPower < playerPower) {
                enemy.setStrength(enemy.getStrength() - playerDamage);
                message = "Вы наносите урон противнику " + playerDamage + " ед.";
            } else {
                message = "Вы парируете удар";
            }

            profileData.setMessage(message);

            if (playerStrength <= 0) {
                Story story = (Story) ApplicationContextHolder.getApplicationContext().getBean("story");
                profileData.setBotState(BotState.SHOW_MAIN_MENU);
                profileData.setCurrentMenu(story.getCombatDefeatParagraph());
                return;
            }

            boolean changeState = false;
            for (Check check : profileData.getCombatChecks()) {
                if (check.test(profileData)) {
                    changeState = true;
                    break;
                }
            }
            if (changeState) {
                profileData.setBotState(BotState.PLAY_SCENARIO);
                profileData.setCombatChecks(new ArrayList<>());
            }
        }
    }
}
