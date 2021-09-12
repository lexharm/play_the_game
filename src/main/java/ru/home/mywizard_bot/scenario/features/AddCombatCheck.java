package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.checks.Check;
import ru.home.mywizard_bot.scenario.checks.EnemyDead;

import java.util.ArrayList;
import java.util.List;

public class AddCombatCheck implements Feature {
    Check check;

    public AddCombatCheck() {
        check = new EnemyDead();
    }

    public AddCombatCheck(Check check) {
        this.check = check;
    }

    @Override
    public void engage(UserProfileData profileData) {
        List<Check> list = new ArrayList<>();
        list.add(check);
        profileData.setCombatChecks(list);
    }
}
