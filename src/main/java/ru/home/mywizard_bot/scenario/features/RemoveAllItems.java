package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;

import java.util.Map;

public class RemoveAllItems implements Feature {

    public RemoveAllItems() {}

    @Override
    public void engage(UserProfileData profileData) {
        for (Map.Entry<String, Integer> entry : profileData.getInventory().entrySet()) {
            profileData.removeItem(entry.getKey());
        }
    }
}
