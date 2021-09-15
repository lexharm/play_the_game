package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.model.UserProfileData;

public class StateCheck extends Check {
    BotState currentState;

    public StateCheck() {}

    public StateCheck(BotState currentState) {
        this.currentState = currentState;
    }

    @Override
    public boolean test(UserProfileData profileData) {
        return profileData.getBotState() == currentState;
    }
}
