package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.model.UserProfileData;

public class ReturnToGame implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        /*if (profileData.getCombatChecks().stream().allMatch(x -> x.test(profileData))) {
            profileData.setBotState(BotState.PLAY_SCENARIO);
        } else {
            profileData.setBotState(BotState.COMBAT);
        }*/
        profileData.setNewParagraph(profileData.getCurrentParagraph());
        profileData.setBotState(BotState.PLAY_SCENARIO);
    }
}
