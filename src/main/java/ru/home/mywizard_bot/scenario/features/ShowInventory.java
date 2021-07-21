package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;

public class ShowInventory implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        profileData.setMessage(profileData.toString());
    }
}
