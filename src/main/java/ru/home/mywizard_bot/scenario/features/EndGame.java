package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;

public class EndGame implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        profileData.setCurrentMenu(10000);
    }
}
