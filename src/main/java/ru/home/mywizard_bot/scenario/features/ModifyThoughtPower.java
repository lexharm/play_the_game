package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;

public class ModifyThoughtPower implements Feature {
    int delta;

    public ModifyThoughtPower(int delta) {
        this.delta = delta;
    }

    @Override
    public void engage(UserProfileData profileData) {
        String status = null;
        if (delta > 0) {
            status = "Сила мысли увеличена на " + delta + " ед.";
        } else if (delta < 0) {
            status = "Сила мысли уменьшена на " + delta + " ед.";
        }
        if (delta != 0) {
            profileData.setThoughtPower(profileData.getThoughtPower() + delta);
            profileData.appendAddStatus(status);
        }
    }
}
