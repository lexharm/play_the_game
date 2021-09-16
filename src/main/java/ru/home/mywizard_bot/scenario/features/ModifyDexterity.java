package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;

public class ModifyDexterity implements Feature {
    int delta;

    public ModifyDexterity(int delta) {
        this.delta = delta;
    }

    @Override
    public void engage(UserProfileData profileData) {
        String status = null;
        if (delta > 0) {
            status = "Ловкость увеличена на " + delta + " ед.";
        } else if (delta < 0) {
            status = "Ловкость уменьшена на " + delta + " ед.";
        }
        if (delta != 0) {
            profileData.setDexterity(profileData.getDexterity() + delta);
            profileData.appendAddStatus(status);
        }
    }
}
