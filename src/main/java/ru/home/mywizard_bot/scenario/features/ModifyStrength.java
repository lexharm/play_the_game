package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;

public class ModifyStrength implements Feature {
    int delta;

    public ModifyStrength(int delta) {
        this.delta = delta;
    }

    @Override
    public void engage(UserProfileData profileData) {
        int initStrength = profileData.getInitStrength();
        int strength = profileData.getStrength();
        int currentDelta = initStrength - strength;
        String status = null;
        if (delta >= currentDelta && currentDelta > 0) {
            delta = currentDelta;
            status = "Здоровье полностью восстановлено.";
        } else if (delta > 0 && currentDelta > 0) {
            status = "Здоровье увеличено на " + delta + " ед.";
        }
        else if (delta < 0 && strength - delta > 0) {
            status = "Здоровье уменьшено на " + Math.abs(delta) + " ед.";
        } else {
            delta = 0;
        }
        if (delta != 0) {
            profileData.setStrength(strength + delta);
            profileData.appendAddStatus(status);
        }
    }
}
