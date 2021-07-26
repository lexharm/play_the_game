package ru.home.mywizard_bot.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.handlers.Handler;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.scenario.actions.Action;
import ru.home.mywizard_bot.service.MainMenuService;
import ru.home.mywizard_bot.service.UsersProfileDataService;

@Component
public class MenuHandler extends Handler {
    protected MenuHandler(UserDataCache userDataCache, UsersProfileDataService profileDataService, MainMenuService mainMenuService, Story story) {
        super(userDataCache, profileDataService, mainMenuService, story);
    }

    @Override
    protected Paragraph getCurrentParagraph(UserProfileData profileData) {
        return profileData.getCurrentMenu(story);
    }

    @Override
    protected Paragraph getNewParagraph(Action link, Paragraph currentParagraph) {
        return story.getMenuParagraph(link);
    }

    @Override
    protected void engageParagraphFeaturesHook_1(Paragraph newParagraph, Paragraph currentParagraph, UserProfileData profileData) {

    }

    @Override
    protected void engageParagraphFeaturesHook_2(Paragraph newParagraph, Paragraph currentParagraph, UserProfileData profileData, boolean paragraphChanged) {
        if ((!newParagraph.getId().equals(currentParagraph.getId())) && (!newParagraph.getId().equals(profileData.getCurrentParagraph().getId()))) {
            newParagraph.engageFeatures(profileData);
        }
    }

    @Override
    protected void processStates(BotState botState, UserProfileData profileData, Paragraph newParagraph) {
        switch (profileData.getBotState()) {
            case PLAY_SCENARIO:
            case COMBAT:
                newParagraph = profileData.getCurrentParagraph();
                break;
            default:
                //SHOW_MAIN_MENU
                profileData.setCurrentMenu(newParagraph);
                break;
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}
