package ru.home.mywizard_bot.botapi.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.scenario.actions.Action;
import ru.home.mywizard_bot.service.ReplyMessagesService;
import ru.home.mywizard_bot.service.UsersProfileDataService;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public abstract class Handler {
    private final UserDataCache userDataCache;
    private final UsersProfileDataService profileDataService;
    private final ReplyMessagesService replyMessagesService;
    protected final Story story;

    protected Handler(UserDataCache userDataCache, UsersProfileDataService profileDataService, ReplyMessagesService replyMessagesService, Story story) {
        this.userDataCache = userDataCache;
        this.profileDataService = profileDataService;
        this.replyMessagesService = replyMessagesService;
        this.story = story;
    }

    public final List<PartialBotApiMethod<?>> handle(BotApiObject botApiObject) {
        List<PartialBotApiMethod<?>> resultList = new ArrayList<>();
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

        log.info("{} {} User:{}, userId: {}, chatId: {}, with text: {}",
                this.getClass().getSimpleName(), (callbackQueryId != null) ? "with callback" : "",
                message.getFrom().getUserName(), userId, chatId, receivedText);

        //UserProfileData profileData = userDataCache.getUserProfileData(userId);
        UserProfileData profileData = profileDataService.getUserProfileData(chatId);

        Paragraph currentParagraph = getCurrentParagraph(profileData);
        /*List<Link> links = new ArrayList<>();
        links.addAll(currentParagraph.getLinks());
        links.addAll(currentParagraph.getInlineLinks());
        links.addAll(story.getExtraLinks(getHandlerName()));*/
        List<Action> links = new ArrayList<>();
        links.addAll(currentParagraph.getMovementLinks());
        links.addAll(currentParagraph.getInlineLinks1());
        links.addAll(story.getExtraLinks(getHandlerName()));

        Paragraph newParagraph = currentParagraph;
        profileData.setNewParagraph(newParagraph);
        boolean paragraphChanged = false;
        Action matchedLink = null;
        for (Action link : links) {
            if ((botApiObject instanceof CallbackQuery && receivedText.equals(link.getId()))
                    || (botApiObject instanceof Message && receivedText.equals(link.getCaption()))) {
                matchedLink = link;
                newParagraph = getNewParagraph(link, currentParagraph);
                profileData.setNewParagraph(newParagraph);
                link.applyEffects(profileData);
                engageParagraphFeaturesHook_1(currentParagraph, profileData);
                processStates(profileData.getBotState(), profileData);
                paragraphChanged = true;
                break;
            }
        }
        engageParagraphFeaturesHook_2(currentParagraph, profileData, paragraphChanged);
        //userDataCache.saveUserProfileData(userId, profileData);
        boolean newMessage = matchedLink == null || matchedLink.isNewMessage();
        if (callbackQueryId != null && !paragraphChanged)
            resultList = replyMessagesService.getIllegalActionMessage(callbackQueryId);
        else
            resultList = replyMessagesService.getMainMenuMessage(chatId, profileData, story, newMessage);
        profileDataService.saveUserProfileData(profileData);
        return resultList;
    }

    protected Paragraph getCurrentParagraph(UserProfileData profileData) {
        return profileData.getCurrentParagraph();
    };

    protected abstract Paragraph getNewParagraph(Action link, Paragraph currentParagraph);

    protected abstract void engageParagraphFeaturesHook_1(Paragraph currentParagraph, UserProfileData profileData);

    protected abstract void engageParagraphFeaturesHook_2(Paragraph currentParagraph, UserProfileData profileData, boolean paragraphChanged);

    protected abstract void processStates(BotState botState, UserProfileData profileData);

    public abstract BotState getHandlerName();
}
