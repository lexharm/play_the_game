package ru.home.mywizard_bot.botapi.handlers.menu;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.handlers.Handler;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.scenario.actions.Action;
import ru.home.mywizard_bot.service.ReplyMessagesService;
import ru.home.mywizard_bot.service.UsersProfileDataService;
import ru.home.mywizard_bot.utils.Emojis;

@Component
public class MenuHandler extends Handler {
    protected MenuHandler(UserDataCache userDataCache, UsersProfileDataService profileDataService, ReplyMessagesService replyMessagesService, Story story) {
        super(userDataCache, profileDataService, replyMessagesService, story);
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
    protected void engageParagraphFeaturesHook_1(Paragraph currentParagraph, UserProfileData profileData, Action matchedLink) {
        if (matchedLink.getCaption().equals("Вернуться в игру")) {
            String status = Emojis.EXCLAMATION + profileData.getUserName() + " завязывает узелочек своего походного " +
                    "мешка, вешает его на палочку и готов к приключениям.";
            profileData.addAddStatus(status);
        }

    }

    @Override
    protected void engageParagraphFeaturesHook_2(Paragraph currentParagraph, UserProfileData profileData, boolean paragraphChanged) {
        try {
            Paragraph newParagraph = profileData.getNewParagraph();
            if ((!newParagraph.getId().equals(currentParagraph.getId())) && (!newParagraph.getId().equals(profileData.getCurrentParagraph().getId()))) {
                newParagraph.applyActions(profileData);
            }
        } catch (Exception e) {}
    }

    @Override
    protected void processStates(BotState botState, UserProfileData profileData) {
        Paragraph newParagraph = profileData.getNewParagraph();
        switch (profileData.getBotState()) {
            case PLAY_SCENARIO:
            case COMBAT:
                //TODO: Looks like here is some bug...
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
