package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;

public class SetDamage implements Feature {
    int value;

    public SetDamage(int value) {
        this.value = value;
    }

    @Override
    public void engage(UserProfileData profileData) {
        profileData.setDamage(value);
    }
}
