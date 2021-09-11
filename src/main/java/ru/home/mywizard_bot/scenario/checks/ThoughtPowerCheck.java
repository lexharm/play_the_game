package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.utils.Condition;

public class ThoughtPowerCheck extends Check {
    int value;
    Condition condition;

    public ThoughtPowerCheck() {}

    public ThoughtPowerCheck(int value, Condition condition) {
        this.value = value;
        this.condition = condition;
    }

    @Override
    public boolean test(UserProfileData profileData) {
        int playerValue = profileData.getThoughtPower();
        switch (condition) {
            case EQUAL: return playerValue == value;
            case NOT_EQUAL: return playerValue != value;
            case MORE: return playerValue > value;
            case LESS: return playerValue < value;
            case MORE_EQUAL: return playerValue >= value;
            case LESS_EQUAL: return playerValue <= value;
        }
        return false;
    }
}
