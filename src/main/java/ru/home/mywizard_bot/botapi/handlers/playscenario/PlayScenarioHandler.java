package ru.home.mywizard_bot.botapi.handlers.playscenario;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.InputMessageHandler;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.MainMenuService;
import ru.home.mywizard_bot.service.ReplyMessagesService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class PlayScenarioHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private final MainMenuService mainMenuService;
    private final Story story;

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
    public List<PartialBotApiMethod<?>> handle(Message message) {
        return processUsersInput(message);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.PLAY_SCENARIO;
    }

    private List<PartialBotApiMethod<?>> processUsersInput(Message inputMsg) {
        log.info("PlayScenarioHandler User:{}, userId: {}, chatId: {}, with text: {}",
                inputMsg.getFrom().getUserName(), inputMsg.getFrom().getId(), inputMsg.getChatId(), inputMsg.getText());
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        UserProfileData profileData = userDataCache.getUserProfileData(userId);

        Paragraph currentParagraph = profileData.getCurrentParagraph();
        List<Link> links = new ArrayList<>();
        links.addAll(currentParagraph.getLinks());
        links.addAll(story.getExtraLinks(BotState.PLAY_SCENARIO));

        Paragraph newParagraph = currentParagraph;
        Link matchedLink = null;
        for (Link link : links) {
            if (usersAnswer.equals(link.getText())) {
                matchedLink = link;
                newParagraph = null;//story.getStoryParagraph(link);
                link.engageFeatures(profileData);
                if (!newParagraph.getId().equals(currentParagraph.getId())) {
                    newParagraph.engageFeatures(profileData);
                }
                switch (profileData.getBotState()) {
                    case COMBAT:
                        profileData.setCurrentParagraph(newParagraph);
                        profileData.setEnemies(newParagraph.getEnemies());
                        break;
                    case SHOW_MAIN_MENU:
                        profileData.setCurrentMenu(newParagraph);
                        break;
                    default:
                        //PLAY_SCENARIO
                        profileData.setCurrentParagraph(newParagraph);
                        break;
                }
                break;
            }
        }
        userDataCache.saveUserProfileData(userId, profileData);
        if (profileData.getBotState() == BotState.COMBAT) {
            newParagraph.setText(newParagraph.getText() + "\n" + profileData.getEnemy().getCombatInfo() + "\n"
                    + profileData.getCombatInfo(true));
        }
        boolean newMessage = matchedLink == null || matchedLink.isNewMessage();
        return mainMenuService.getMainMenuMessage(chatId, newParagraph, profileData, story, newMessage);
    }

    //TODO: use it for advanced features
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
