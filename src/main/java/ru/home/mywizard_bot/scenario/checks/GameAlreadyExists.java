package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;

public class GameAlreadyExists extends Check {
    @Override
    public boolean test(UserProfileData profileData) {
        if (profileData.getCurrentParagraph() != null)
            return true;
        return false;
    }
}
