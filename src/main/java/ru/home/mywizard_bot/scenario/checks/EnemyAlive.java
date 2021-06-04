package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;

public class EnemyAlive extends Check {
    @Override
    public boolean test(UserProfileData profileData) {
        return profileData.getEnemy().getStrength() > 0;
    }
}
