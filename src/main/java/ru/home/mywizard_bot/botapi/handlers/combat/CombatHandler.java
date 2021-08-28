package ru.home.mywizard_bot.botapi.handlers.combat;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.handlers.Handler;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Combat;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.scenario.actions.Action;
import ru.home.mywizard_bot.service.ReplyMessagesService;
import ru.home.mywizard_bot.service.UsersProfileDataService;

@Component
public class CombatHandler extends Handler {
    protected CombatHandler(UserDataCache userDataCache, UsersProfileDataService profileDataService, ReplyMessagesService replyMessagesService, Story story) {
        super(userDataCache, profileDataService, replyMessagesService, story);
    }

    @Override
    protected Paragraph getCurrentParagraph(UserProfileData profileData) {
        return profileData.getCurrentCombatTurn();
    }

    @Override
    protected Paragraph getNewParagraph(Action link, Paragraph currentParagraph) {
        return new Paragraph();
    }

    @Override
    protected void engageParagraphFeaturesHook_1(Paragraph newParagraph, Paragraph currentParagraph, UserProfileData profileData) {
        if (profileData.getBotState() != BotState.SHOW_MAIN_MENU) {
            if (profileData.getCombatChecks().stream().allMatch(x -> x.test(profileData)) || profileData.getStrength() <= 0) {
                profileData.setBotState(BotState.PLAY_SCENARIO);
            } else {
                profileData.incCombatTurn();
            }
        }
    }

    @Override
    protected void engageParagraphFeaturesHook_2(Paragraph newParagraph, Paragraph currentParagraph, UserProfileData profileData, boolean paragraphChanged) {

    }

    @Override
    protected void processStates(BotState botState, UserProfileData profileData, Paragraph newParagraph) {
        switch (profileData.getBotState()) {
            case PLAY_SCENARIO:
                newParagraph.addText(profileData.getCombatStatus());
                profileData.setCurrentCombatTurn(newParagraph);
                Combat.combatEnd(profileData);
                break;
            case SHOW_MAIN_MENU:
                profileData.setCurrentMenu(newParagraph);
                break;
            default:
                //COMBAT
                newParagraph.addText(profileData.getCombatStatus());
                profileData.setCurrentCombatTurn(newParagraph);
                Combat.newTurn(profileData);
                break;
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.COMBAT;
    }
}
