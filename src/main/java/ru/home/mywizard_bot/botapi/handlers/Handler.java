package ru.home.mywizard_bot.botapi.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.scenario.Enemy;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.MainMenuService;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public abstract class Handler {
    private final UserDataCache userDataCache;
    private final MainMenuService mainMenuService;
    private final Story story;

    protected Handler(UserDataCache userDataCache, MainMenuService mainMenuService, Story story) {
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.story = story;
    }

    public final List<PartialBotApiMethod<?>> handle(BotApiObject botApiObject) {
        int userId;
        String receivedText;
        Message message;
        String callbackQueryId = null;
        if (botApiObject instanceof CallbackQuery) {
            CallbackQuery callbackQuery = (CallbackQuery) botApiObject;
            message = callbackQuery.getMessage();
            userId = callbackQuery.getFrom().getId();
            receivedText = callbackQuery.getData();
            callbackQueryId = callbackQuery.getId();
        } else {
            message = (Message) botApiObject;
            userId = message.getFrom().getId();
            receivedText = message.getText();
        }
        long chatId = message.getChatId();

        log.info("{} User:{}, userId: {}, chatId: {}, with text: {}",
                this.getClass().getSimpleName(), message.getFrom().getUserName(), userId, chatId, receivedText);

        UserProfileData profileData = userDataCache.getUserProfileData(userId);

        Paragraph currentParagraph = profileData.getCurrentParagraph();
        List<Link> links = new ArrayList<>();
        links.addAll(currentParagraph.getLinks());
        links.addAll(story.getExtraLinks(getHandlerName()));

        Paragraph newParagraph = currentParagraph;
        boolean isParagraphChanged = false;
        Link matchedLink = null;
        for (Link link : links) {
            if ((botApiObject instanceof CallbackQuery && receivedText.equals(link.getId()))
                    || (botApiObject instanceof Message && receivedText.equals(link.getText()))) {
                matchedLink = link;
                newParagraph = story.getCombatParagraph(link, currentParagraph);
                link.engageFeatures(profileData);
                if (!newParagraph.getId().equals(currentParagraph.getId())) {
                    newParagraph.engageFeatures(profileData);
                }
                processStates(profileData.getBotState(), profileData, newParagraph);
                isParagraphChanged = true;
                break;
            }
        }
        boolean newMessage = matchedLink == null || matchedLink.isNewMessage();
        userDataCache.saveUserProfileData(userId, profileData);
        if (!isParagraphChanged && callbackQueryId != null)
            return mainMenuService.getIllegalActionMessage(callbackQueryId);
        else
            return mainMenuService.getMainMenuMessage(chatId, newParagraph, profileData, story, newMessage);
    }

    public abstract void processStates(BotState botState, UserProfileData profileData, Paragraph newParagraph);

    public abstract BotState getHandlerName();
}
