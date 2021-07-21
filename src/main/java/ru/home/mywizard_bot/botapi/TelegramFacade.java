package ru.home.mywizard_bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.service.MainMenuService;
import ru.home.mywizard_bot.service.UsersProfileDataService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Tonkikh
 */
@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private UsersProfileDataService profileDataService;
    private MainMenuService mainMenuService;
    @Value("${telegrambot.sleep}")
    private int sleepTime;

    public int getSleepTime() {
        return sleepTime;
    }


    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, UsersProfileDataService profileDataService, MainMenuService mainMenuService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.profileDataService = profileDataService;
    }

    public UserDataCache getUserDataCache() {
        return userDataCache;
    }

    public UsersProfileDataService getProfileDataService() {
        return profileDataService;
    }

    /*public BotApiMethod<?> handleUpdate(Update update) {
            SendMessage replyMessage = null;
            if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                        callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
                return processCallbackQuery(callbackQuery);
            }
            Message message = update.getMessage();
            if (message != null && message.hasText()) {
                log.info("New message from User:{}, userId: {}, chatId: {},  with text: {}",
                        message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
                replyMessage = handleInputMessage(message);
            }
            return replyMessage;
        }*/
    public List<PartialBotApiMethod<?>> handleUpdate(Update update) {
        List<PartialBotApiMethod<?>> replyMessagesList = null;
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User: {}, userId: {}, with data: {}", update.getCallbackQuery().getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), update.getCallbackQuery().getData());
            replyMessagesList = processCallbackQuery(callbackQuery);
        }
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, userId: {}, chatId: {}, with text: {}",
                    message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
            replyMessagesList = handleInputMessage(message);
        }
        log.info("replyMessagesList has size: {}", replyMessagesList != null ? replyMessagesList.size() : "null");
        //TODO: It's needed to do smthng with unknown updates. Where is it from?
        if (replyMessagesList == null) {
            replyMessagesList = new ArrayList<>();
            replyMessagesList.add(new SendMessage().setText("Catched unknown update").setChatId(new Long(149037203)));
        }
        return replyMessagesList;
    }

    /*@Deprecated
    private SendMessage handleInputMessage_old(Message message) {
        String inputMsg = message.getText();
        long chatId = message.getChatId();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;
        switch (inputMsg) {
            case "/start":
                botState = BotState.SHOW_MAIN_MENU;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }
        userDataCache.setUsersCurrentBotState(userId, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);
        return replyMessage;
    }*/

    private List<PartialBotApiMethod<?>> handleInputMessage(Message message) {
        String inputMsg = message.getText();
        long chatId = message.getChatId();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;
        switch (inputMsg) {
            case "/start":
                //userDataCache.clearCache();
                profileDataService.deleteByChatId(chatId);
                UserProfileData profileData = new UserProfileData();
                profileData.setChatId(chatId);
                profileData.setBotState(BotState.SHOW_MAIN_MENU);
                profileDataService.saveUserProfileData(profileData);
                botState = BotState.SHOW_MAIN_MENU;
                break;
            default:
                //botState = userDataCache.getUsersCurrentBotState(userId);
                botState = profileDataService.getUserBotState(userId);
                break;
        }
        //userDataCache.setUsersCurrentBotState(userId, botState);
        return botStateContext.processInputMessage(botState, message);
    }

    private List<PartialBotApiMethod<?>> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();
        BotState botState;
        switch (buttonQuery.getData()) {
            case "/start":
                profileDataService.deleteByChatId(chatId);
                UserProfileData profileData = new UserProfileData();
                profileData.setChatId(chatId);
                profileData.setBotState(BotState.SHOW_MAIN_MENU);
                profileDataService.saveUserProfileData(profileData);
                botState = BotState.SHOW_MAIN_MENU;
                break;
            default:
                //botState = userDataCache.getUsersCurrentBotState(userId);
                botState = profileDataService.getUserBotState(chatId);
                break;
        }
        //userDataCache.setUsersCurrentBotState(userId, botState);
        //BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтесь главным меню");
        //List<PartialBotApiMethod<?>> callBackAnswer = botStateContext.processCallbackQuery(botState, buttonQuery);
        List<PartialBotApiMethod<?>> callBackAnswer = botStateContext.processInputMessage(botState, buttonQuery);
        /*List<List<InlineKeyboardButton>> inlineKeyboard = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton().setText("Ok!");
        button.setCallbackData("123");
        List<InlineKeyboardButton> buttonList = new ArrayList<>();
        buttonList.add(button);
        inlineKeyboard.add(buttonList);
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(inlineKeyboard);
        BotApiMethod<?> callBackAnswer = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(buttonQuery.getMessage().getMessageId())
                .setText("Hello there, general Kenobi!")
                .setReplyMarkup(inlineKeyboardMarkup);*/
        /*BotApiMethod<?> callBackAnswer = new EditMessageReplyMarkup()
                .setReplyMarkup(inlineKeyboardMarkup)
                .setMessageId(buttonQuery.getMessage().getMessageId())
                .setChatId(chatId);*/

        /*//From Destiny choose buttons
        if (buttonQuery.getData().equals("buttonYes")) {
            callBackAnswer = new SendMessage(chatId, "Как тебя зовут ?");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        } else if (buttonQuery.getData().equals("buttonNo")) {
            callBackAnswer = sendAnswerCallbackQuery("Возвращайся, когда будешь готов", false, buttonQuery);
        } else if (buttonQuery.getData().equals("buttonIwillThink")) {
            callBackAnswer = sendAnswerCallbackQuery("Данная кнопка не поддерживается", true, buttonQuery);
        }

        //From Gender choose buttons
        else if (buttonQuery.getData().equals("buttonMan")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("М");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
            callBackAnswer = new SendMessage(chatId, "Твоя любимая цифра");
        } else if (buttonQuery.getData().equals("buttonWoman")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("Ж");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_COLOR);
            callBackAnswer = new SendMessage(chatId, "Твоя любимая цифра");
        } else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }*/
        return callBackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }
}
