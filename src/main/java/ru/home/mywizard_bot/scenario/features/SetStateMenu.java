package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;

public class SetStateMenu implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        profileData.setBotState(BotState.SHOW_MAIN_MENU);
    }
}
