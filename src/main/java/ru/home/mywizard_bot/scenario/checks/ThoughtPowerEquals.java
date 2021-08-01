package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;

public class ThoughtPowerEquals extends Check {

    private int thoughtPower;

    public ThoughtPowerEquals(int thoughtPower) {
        this.thoughtPower = thoughtPower;
    }

    @Override
    public boolean test(UserProfileData profileData) {
        return profileData.getThoughtPower() == thoughtPower;
    }
}
