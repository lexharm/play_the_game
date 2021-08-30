package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.model.UserProfileData;

public class SetStateCombat implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        profileData.setBotState(BotState.COMBAT);
    }
}
