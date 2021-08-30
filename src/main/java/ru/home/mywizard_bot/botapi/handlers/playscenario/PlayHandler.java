package ru.home.mywizard_bot.botapi.handlers.playscenario;

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
public class PlayHandler extends Handler {
    protected PlayHandler(UserDataCache userDataCache, UsersProfileDataService profileDataService, ReplyMessagesService replyMessagesService, Story story) {
        super(userDataCache, profileDataService, replyMessagesService, story);
    }

    @Override
    protected Paragraph getNewParagraph(Action link, Paragraph currentParagraph) {
        return story.getStoryParagraph(link);
    }

    @Override
    protected void engageParagraphFeaturesHook_1(Paragraph newParagraph, Paragraph currentParagraph, UserProfileData profileData) {
        if (!newParagraph.getId().equals(currentParagraph.getId())) {
            newParagraph.applyActions(profileData);
        }
    }

    @Override
    protected void engageParagraphFeaturesHook_2(Paragraph newParagraph, Paragraph currentParagraph, UserProfileData profileData, boolean paragraphChanged) {
        if (profileData.getBotState() == BotState.COMBAT) {
            /*newParagraph.setText(newParagraph.getText() + "\n" + profileData.getEnemy().getCombatInfo() + "\n"
                    + profileData.getCombatInfo());*/
            return;
        }
    }

    @Override
    protected void processStates(BotState botState, UserProfileData profileData, Paragraph newParagraph) {
        switch (profileData.getBotState()) {
            case COMBAT:
                profileData.setCurrentParagraph(newParagraph);
                profileData.setCurrentCombatTurn(newParagraph);
                profileData.setEnemies(newParagraph.getEnemies());
                profileData.calcPowerCombatRange();
                Combat.newTurn(profileData);
                newParagraph.substitute(profileData.getCurrentCombatTurn());
                break;
            case SHOW_MAIN_MENU:
                profileData.setCurrentMenu(newParagraph);
                break;
            default:
                //PLAY_SCENARIO
                profileData.setCurrentParagraph(newParagraph);
                break;
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.PLAY_SCENARIO;
    }
}
