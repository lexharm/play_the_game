package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;

public class PlayerDead extends Check {
    @Override
    public boolean test(UserProfileData profileData) {
        return profileData.getStrength() <= 0;
    }
}
