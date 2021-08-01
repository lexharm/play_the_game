package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;

public class IncThoughtPower implements Feature {

    private int value;

    public IncThoughtPower() {
        value = 1;
    }

    public IncThoughtPower(int value) {
        this.value = value;
    }

    @Override
    public void engage(UserProfileData profileData) {
        profileData.setThoughtPower(profileData.getThoughtPower() + value);
    }
}
