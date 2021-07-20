package ru.home.mywizard_bot.botapi.handlers.combat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.CallbackHandler;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.scenario.Enemy;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.MainMenuService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CombatCallbackHandler implements CallbackHandler {
    private final UserDataCache userDataCache;
    private final MainMenuService mainMenuService;
    private final Story story;

    public CombatCallbackHandler(UserDataCache userDataCache, MainMenuService mainMenuService, Story story) {
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.story = story;
    }

    @Override
    public List<PartialBotApiMethod<?>> handle(CallbackQuery callbackQuery) {
        return processUsersInput(callbackQuery);
    }

    private List<PartialBotApiMethod<?>> processUsersInput(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        log.info("CombatCallbackHandler User:{}, userId: {}, chatId: {}, with text: {}",
                message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), callbackQuery.getData());
        String usersAnswer = callbackQuery.getData();
        int userId = callbackQuery.getFrom().getId();
        long chatId = message.getChatId();
        UserProfileData profileData = userDataCache.getUserProfileData(userId);

        Paragraph currentParagraph = profileData.getCurrentParagraph();
        List<Link> links = new ArrayList<>();
        links.addAll(currentParagraph.getInlineLinks());
        links.addAll(story.getExtraLinks(BotState.COMBAT));

        Paragraph newParagraph = currentParagraph;
        boolean isParagraphChanged = false;
        Link matchedLink = null;
        for (Link link : links) {
            if (usersAnswer.equals(link.getId())) {
                matchedLink = link;
                newParagraph = story.getCombatParagraph(link, currentParagraph);
                link.engageFeatures(profileData);
                if (!newParagraph.getId().equals(currentParagraph.getId())) {
                    newParagraph.engageFeatures(profileData);
                }
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
                isParagraphChanged = true;
                break;
            }
        }
        userDataCache.saveUserProfileData(userId, profileData);
        boolean newMessage = matchedLink == null || matchedLink.isNewMessage();
        if (isParagraphChanged)
            return mainMenuService.getMainMenuMessage(chatId, newParagraph, profileData, story, newMessage);
        else
            return mainMenuService.getIllegalActionMessage(callbackQuery);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.COMBAT;
    }
}
