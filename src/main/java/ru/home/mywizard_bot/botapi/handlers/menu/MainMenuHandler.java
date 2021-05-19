package ru.home.mywizard_bot.botapi.handlers.menu;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.InputMessageHandler;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.MainMenuService;
import ru.home.mywizard_bot.service.ReplyMessagesService;

@Slf4j
@Component
public class MainMenuHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;
    private Story story;

    public MainMenuHandler(UserDataCache userDataCache, ReplyMessagesService messagesService, MainMenuService mainMenuService, Story story) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
        this.story = story;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message message) {
        log.info("MainMenuHandler User:{}, userId: {}, chatId: {}, with text: {}",
                message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
        String menuButton = message.getText();
        if (menuButton.equals("Назад")) {
            int userId = message.getFrom().getId();
            long chatId = message.getChatId();
            UserProfileData profileData = userDataCache.getUserProfileData(userId);
            Paragraph newParagraph = story.getParagraph(profileData.getCurrentParagraph());
            if (newParagraph.isCombat()) {
                userDataCache.setUsersCurrentBotState(userId, BotState.COMBAT);
                return mainMenuService.getMainMenuMessageForCombat(chatId, "", newParagraph, profileData.getEnemy(), profileData.getStrength());
            } else {
                userDataCache.setUsersCurrentBotState(userId, BotState.PLAY_SCENARIO);
                return mainMenuService.getMainMenuMessage(message.getChatId(), newParagraph, profileData);
            }
        }
        return mainMenuService.getMainMenuMessage(message.getChatId(), messagesService.getReplyText("reply.showMainMenu"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}
