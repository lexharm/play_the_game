package ru.home.mywizard_bot.botapi.handlers.playscenario;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
public class PlayScenarioHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;
    private Story story;

    public PlayScenarioHandler(UserDataCache userDataCache,
                               ReplyMessagesService messagesService,
                               MainMenuService mainMenuService,
                               Story story) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
        this.story = story;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.PLAY_SCENARIO;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        log.info("PlayScenarioHandler User:{}, userId: {}, chatId: {}, with text: {}",
                inputMsg.getFrom().getUserName(), inputMsg.getFrom().getId(), inputMsg.getChatId(), inputMsg.getText());
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        int currentParagraph = profileData.getCurrentParagraph();

        SendMessage replyToUser = null;

        Paragraph newParagraph = story.getParagraph(currentParagraph);
        List<Link> links = new ArrayList<>();
        links.addAll(story.getExtraLinks().get(profileData.getBotState()));
        Paragraph paragraph = story.getParagraph(currentParagraph);
        links.addAll(paragraph.getLinks());

        for (Link link : links) {
            if (usersAnswer.equals(link.getText())) {
                try {
                    newParagraph = story.getParagraph(link);
                } catch (NoLinkException e) {
                    newParagraph = story.getParagraph(-1);
                    userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
                }
                link.engageFeatures(profileData);
                if (newParagraph.isCombat()) {
                    userDataCache.setUsersCurrentBotState(userId, BotState.COMBAT);
                    profileData.setEnemy(newParagraph.getEnemy());
                }
                profileData.setCurrentParagraph(newParagraph.getId());
                userDataCache.saveUserProfileData(userId, profileData);
                break;
            }
        }

        /*switch (usersAnswer) {
            case "Меню":
                newParagraph = story.getParagraph(10000);
                userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
                break;
            case "Листок путешественника":
                newParagraph = new Paragraph(1000000, profileData.toString());
                links.add(new Link("Восстановить здоровье едой", 1000000));
                links.add(new Link("Телепатические способности", 1000000));
                links.add(new Link("Назад", currentParagraph));
                newParagraph.setLinks(links);
                userDataCache.setUsersCurrentBotState(userId, BotState.INVENTORY);
                break;
            default:
                Paragraph paragraph = story.getParagraph(currentParagraph);
                for (Link link : paragraph.getLinks()) {
                    if (usersAnswer.equals(link.getText())) {
                        try {
                            newParagraph = story.getParagraph(link);
                        } catch (NoLinkException e) {
                            newParagraph = story.getParagraph(-1);
                            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
                        }
                        link.engageFeatures(profileData);
                        if (newParagraph.isCombat()) {
                            userDataCache.setUsersCurrentBotState(userId, BotState.COMBAT);
                            profileData.setEnemy(newParagraph.getEnemy());
                        }
                        profileData.setCurrentParagraph(newParagraph.getId());
                        userDataCache.saveUserProfileData(userId, profileData);
                        break;
                    }
                }
                break;
        }*/

        newParagraph.engageFeatures(profileData);
        userDataCache.saveUserProfileData(userId, profileData);

        if (userDataCache.getUsersCurrentBotState(userId) == BotState.COMBAT) {
            replyToUser = mainMenuService.getMainMenuMessageForCombat(chatId, newParagraph.getText(), newParagraph, newParagraph.getEnemy(), profileData.getStrength());
        } else {
            replyToUser = mainMenuService.getMainMenuMessage(chatId, newParagraph, profileData, story);
        }
        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineMessageButtons(Paragraph paragraph) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = new InlineKeyboardButton().setText("Да");
        InlineKeyboardButton buttonNo = new InlineKeyboardButton().setText("Нет, спасибо");
        InlineKeyboardButton buttonIwillThink = new InlineKeyboardButton().setText("Я подумаю");
        InlineKeyboardButton buttonIdontKnow = new InlineKeyboardButton().setText("Еще не определился");

        //Every button must have callBackData, or else not work !
        buttonYes.setCallbackData("buttonYes");
        buttonNo.setCallbackData("buttonNo");
        buttonIwillThink.setCallbackData("buttonIwillThink");
        buttonIdontKnow.setCallbackData("-");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonYes);
        keyboardButtonsRow1.add(buttonNo);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonIwillThink);
        keyboardButtonsRow2.add(buttonIdontKnow);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
