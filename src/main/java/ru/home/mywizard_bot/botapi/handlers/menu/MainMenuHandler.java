package ru.home.mywizard_bot.botapi.handlers.menu;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.InputMessageHandler;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.NoLinkException;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.MainMenuService;
import ru.home.mywizard_bot.service.ReplyMessagesService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MainMenuHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private final MainMenuService mainMenuService;
    private final Story story;

    public MainMenuHandler(UserDataCache userDataCache, ReplyMessagesService messagesService, MainMenuService mainMenuService, Story story) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
        this.story = story;
    }

    @Override
    public List<BotApiMethod<?>> handle(Message message) {
        return processUsersInput(message);
    }

    private List<BotApiMethod<?>> processUsersInput(Message message) {
        log.info("MainMenuHandler User:{}, userId: {}, chatId: {}, with text: {}",
                message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
        String usersAnswer = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        UserProfileData profileData = userDataCache.getUserProfileData(userId);

        Paragraph currentMenu = profileData.getCurrentMenu(story);
        List<Link> links = new ArrayList<>();
        links.addAll(currentMenu.getLinks());
        links.addAll(story.getExtraLinks(BotState.SHOW_MAIN_MENU));

        Paragraph newParagraph = currentMenu;
        Link matchedLink = null;
        for (Link link : links) {
            if (usersAnswer.equals(link.getText())) {
                matchedLink = link;
                newParagraph = story.getMenuParagraph(link);
                link.engageFeatures(profileData);
                switch (profileData.getBotState()) {
                    case PLAY_SCENARIO:
                    case COMBAT:
                        newParagraph = profileData.getCurrentParagraph();
                        break;
                    default:
                        //SHOW_MAIN_MENU
                        profileData.setCurrentMenu(newParagraph);
                        break;
                }
                break;
            }
        }
        if ((!newParagraph.getId().equals(currentMenu.getId())) && (!newParagraph.getId().equals(profileData.getCurrentParagraph().getId()))) {
            newParagraph.engageFeatures(profileData);
        }
        userDataCache.saveUserProfileData(userId, profileData);
        boolean newMessage = matchedLink == null || matchedLink.isNewMessage();
        List<BotApiMethod<?>> replyMessagesList = mainMenuService.getMainMenuMessage(chatId, newParagraph, profileData, story, newMessage);

        //replyMessage.setReplyMarkup(getInlineMessageButtons());

        return replyMessagesList;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = new InlineKeyboardButton().setText("Да");
        InlineKeyboardButton buttonNo = new InlineKeyboardButton().setText("Нет, спасибо");
        /*InlineKeyboardButton buttonIwillThink = new InlineKeyboardButton().setText("Я подумаю");
        InlineKeyboardButton buttonIdontKnow = new InlineKeyboardButton().setText("Еще не определился");*/

        //Every button must have callBackData, or else not work !
        buttonYes.setCallbackData("buttonYes");
        buttonNo.setCallbackData("buttonNo");
        /*buttonIwillThink.setCallbackData("buttonIwillThink");
        buttonIdontKnow.setCallbackData("-");*/

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonYes);
        keyboardButtonsRow1.add(buttonNo);

        /*List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonIwillThink);
        keyboardButtonsRow2.add(buttonIdontKnow);*/

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        //rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
