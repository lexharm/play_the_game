package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;

public class RemoveAllItems implements Feature {

    public RemoveAllItems() {}

    @Override
    public void engage(UserProfileData profileData) {
        profileData.removeAllItems();
    }
}
