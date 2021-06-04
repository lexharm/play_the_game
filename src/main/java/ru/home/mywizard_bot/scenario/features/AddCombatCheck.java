package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.checks.Check;
import ru.home.mywizard_bot.scenario.checks.EnemyDead;

import java.util.ArrayList;
import java.util.List;

public class AddCombatCheck implements Feature {

    @Override
    public void engage(UserProfileData profileData) {
        List<Check> list = new ArrayList<>();
        list.add(new EnemyDead());
        profileData.setCombatChecks(list);
    }
}
