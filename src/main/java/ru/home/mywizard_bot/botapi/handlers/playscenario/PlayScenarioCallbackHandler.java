package ru.home.mywizard_bot.botapi.handlers.playscenario;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.CallbackHandler;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.MainMenuService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PlayScenarioCallbackHandler implements CallbackHandler {
    private final UserDataCache userDataCache;
    private final MainMenuService mainMenuService;
    private final Story story;

    public PlayScenarioCallbackHandler(UserDataCache userDataCache, MainMenuService mainMenuService, Story story) {
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.story = story;
    }

    @Override
    public List<BotApiMethod<?>> handle(CallbackQuery callbackQuery) {
        return processUsersInput(callbackQuery);
    }

    private List<BotApiMethod<?>> processUsersInput(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        log.info("PlayScenarioCallbackHandler User:{}, userId: {}, chatId: {}, with text: {}",
                message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), callbackQuery.getData());
        String usersAnswer = callbackQuery.getData();
        int userId = callbackQuery.getFrom().getId();
        long chatId = message.getChatId();
        UserProfileData profileData = userDataCache.getUserProfileData(userId);

        Paragraph currentParagraph = profileData.getCurrentParagraph();
        List<Link> links = new ArrayList<>();
        links.addAll(currentParagraph.getInlineLinks());
        //links.addAll(story.getExtraLinks(BotState.SHOW_MAIN_MENU));

        Paragraph newParagraph = currentParagraph;
        boolean isParagraphChanged = false;
        for (Link link : links) {
            if (usersAnswer.equals(link.getId())) {
                newParagraph = story.getMenuParagraph(link);
                link.engageFeatures(profileData);
                if (!newParagraph.getId().equals(currentParagraph.getId())) {
                    newParagraph.engageFeatures(profileData);
                }
                switch (profileData.getBotState()) {
                    case COMBAT:
                        profileData.setCurrentParagraph(newParagraph);
                        profileData.setEnemy(newParagraph.getEnemy());
                        break;
                    case SHOW_MAIN_MENU:
                        profileData.setCurrentMenu(newParagraph);
                        break;
                    default:
                        //PLAY_SCENARIO
                        profileData.setCurrentParagraph(newParagraph);
                        break;
                }
                isParagraphChanged = true;
                break;
            }
        }
        userDataCache.saveUserProfileData(userId, profileData);
        if (profileData.getBotState() == BotState.COMBAT) {
            newParagraph.setText(newParagraph.getText() + "\n" + profileData.getEnemy().getCombatInfo() + "\n"
                    + profileData.getCombatInfo());
        }
        if (isParagraphChanged)
            return mainMenuService.getMainMenuMessage(chatId, newParagraph, profileData, story);
        else
            return mainMenuService.getIllegalActionMessage(callbackQuery);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.PLAY_SCENARIO;
    }
}
