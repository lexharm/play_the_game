package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;

public class EnemyDead extends Check {
    @Override
    public boolean test(UserProfileData profileData) {
        return profileData.getEnemies().stream().allMatch(x -> x.getStrength() <= 0);
    }
}
