package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;

public class EnemyDead extends Check {
    int value;

    public EnemyDead() {
        value = 0;
    }

    public EnemyDead(int value) {
        this.value = value;
    }

    @Override
    public boolean test(UserProfileData profileData) {
        return profileData.getEnemies().stream().allMatch(x -> x.getStrength() <= value);
    }
}
