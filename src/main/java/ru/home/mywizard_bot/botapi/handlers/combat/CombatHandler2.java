package ru.home.mywizard_bot.botapi.handlers.combat;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.handlers.Handler;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.scenario.Enemy;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.MainMenuService;

@Component
public class CombatHandler2 extends Handler {
    protected CombatHandler2(UserDataCache userDataCache, MainMenuService mainMenuService, Story story) {
        super(userDataCache, mainMenuService, story);
    }

    @Override
    protected Paragraph getNewParagraph(Link link, Paragraph currentParagraph) {
        return story.getCombatParagraph(link, currentParagraph);
    }

    @Override
    protected void engageParagraphFeaturesHook_1(Paragraph newParagraph, Paragraph currentParagraph, UserProfileData profileData) {
        if (!newParagraph.getId().equals(currentParagraph.getId())) {
            newParagraph.engageFeatures(profileData);
        }
    }

    @Override
    protected void engageParagraphFeaturesHook_2(Paragraph newParagraph, Paragraph currentParagraph, UserProfileData profileData, boolean paragraphChanged) {

    }

    @Override
    protected void processStates(BotState botState, UserProfileData profileData, Paragraph newParagraph) {
        switch (profileData.getBotState()) {
            case PLAY_SCENARIO:
                profileData.setCurrentParagraph(newParagraph);
                profileData.setEnemy(new Enemy("dummy", "dummy", 0, 0, 0));
                if (newParagraph.getPostText().length() > 0) {
                    newParagraph.setText(newParagraph.getPostText());
                }
                break;
            case SHOW_MAIN_MENU:
                profileData.setCurrentMenu(newParagraph);
                break;
            default:
                //COMBAT
                newParagraph.setText(profileData.getMessage() + "\n" + profileData.getCombatInfo() + "\n"
                        + profileData.getEnemy().getCombatInfo());
                break;
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.COMBAT;
    }
}
