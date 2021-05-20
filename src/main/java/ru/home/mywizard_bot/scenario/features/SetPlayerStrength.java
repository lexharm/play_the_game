package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;

public class SetPlayerStrength implements Feature {
    int strength;

    public SetPlayerStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public void engage(UserProfileData profileData) {
        profileData.setStrength(strength);
    }
}
