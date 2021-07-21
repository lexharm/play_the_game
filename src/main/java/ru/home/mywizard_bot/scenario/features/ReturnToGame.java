package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.model.UserProfileData;

public class ReturnToGame implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        if (profileData.getEnemy().getStrength() > 0) {
            profileData.setBotState(BotState.COMBAT);
        } else {
            profileData.setBotState(BotState.PLAY_SCENARIO);
        }
    }
}
