package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.ApplicationContextHolder;

public class EndGame implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        Story story = (Story) ApplicationContextHolder.getApplicationContext().getBean("story");
        profileData.setCurrentMenu(story.getNoLinkParagraph());
        profileData.setActiveGame(false);
        profileData.setBotState(BotState.SHOW_MAIN_MENU);
    }
}
