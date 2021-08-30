package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;

public class ActiveGameCheck extends Check {
    @Override
    public boolean test(UserProfileData profileData) {
        return profileData.isActiveGame();
    }
}
