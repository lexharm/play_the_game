package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.Paragraph;

public class ReturnToGame implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        if (profileData.getEnemy() != null) {
            profileData.setBotState(BotState.COMBAT);
        } else {
            profileData.setBotState(BotState.PLAY_SCENARIO);
        }
    }
}
