package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;

public class CombatBlock implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        profileData.setCombatStatus("Ход " + profileData.getCombatTurn() + ": Игрок занимает оборону.");
    }
}
