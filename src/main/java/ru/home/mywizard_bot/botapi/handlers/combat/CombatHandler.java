package ru.home.mywizard_bot.botapi.handlers.combat;

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
import ru.home.mywizard_bot.scenario.*;
import ru.home.mywizard_bot.service.MainMenuService;
import ru.home.mywizard_bot.service.ReplyMessagesService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CombatHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;
    private Story story;

    public CombatHandler(UserDataCache userDataCache,
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
        return BotState.COMBAT;
    }

    private SendMessage processUsersInput(Message inputMsg) {
        log.info("CombatHandler User:{}, userId: {}, chatId: {}, with text: {}",
                inputMsg.getFrom().getUserName(), inputMsg.getFrom().getId(), inputMsg.getChatId(), inputMsg.getText());
        String usersAnswer = inputMsg.getText();
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();
        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        int currentParagraph = profileData.getCurrentParagraph();
        if (currentParagraph == 0)
            currentParagraph = 999;

        Paragraph newParagraph = null;

        if (usersAnswer != null) {
            Paragraph paragraph = story.getParagraph(currentParagraph);
            List<Link> links = paragraph.getLinks();
            for (Link link : links) {
                if (usersAnswer.equals(link.getText())){
                    newParagraph = story.getParagraph(link);
                    link.reward(profileData);
                    if (newParagraph.isCombat()) {
                        userDataCache.setUsersCurrentBotState(userId, BotState.COMBAT);
                        profileData.setEnemy(newParagraph.getEnemy());
                    } else {
                        userDataCache.setUsersCurrentBotState(userId, BotState.PLAY_SCENARIO);
                    }
                    profileData.setCurrentParagraph(newParagraph.getId());
                    userDataCache.saveUserProfileData(userId, profileData);
                    break;
                }
            }
        }

        SendMessage replyToUser = null;
        String replyText = "";
        Enemy enemy = profileData.getEnemy();
        int playerStrength = profileData.getStrength();

        if (newParagraph == null) {

            int playerDamage = profileData.getDamage();
            int playerDexterity = profileData.getDexterity();

            //BotState botState = userDataCache.getUsersCurrentBotState(userId);


            newParagraph = story.getParagraph(currentParagraph);

            if (enemy.getStrength() > 0 && playerStrength > 0) {
                int enemyPower = Dice.roll() * 2 + enemy.getDexterity();
                int playerPower = Dice.roll() * 2 + playerDexterity;
                if (enemyPower > playerPower) {
                    playerStrength -= enemy.getDamage();
                    profileData.setStrength(playerStrength);
                    replyText = enemy.getName() + " наносит удар. Урон " + enemy.getDamage() + " ед.";
                } else if (enemyPower < playerPower) {
                    enemy.setStrength(enemy.getStrength() - playerDamage);
                    replyText = "Вы наносите удар " + playerDamage + " ед.";
                } else {
                    replyText = "Вы парируете удар";
                }

                if (playerStrength <= 0) {
                    newParagraph = story.getParagraph(-2);
                    userDataCache.setUsersCurrentBotState(userId, BotState.PLAY_SCENARIO);
                } else if (enemy.getStrength() <= 0) {
                    newParagraph = story.getParagraph(1000);
                    userDataCache.setUsersCurrentBotState(userId, BotState.PLAY_SCENARIO);
                }

                profileData.setCurrentParagraph(newParagraph.getId());
                userDataCache.saveUserProfileData(userId, profileData);
            }
        }
            if (userDataCache.getUsersCurrentBotState(userId) == BotState.COMBAT) {
                replyToUser = mainMenuService.getMainMenuMessageForCombat(chatId, replyText, newParagraph, enemy, playerStrength);
            } else {
                replyToUser = mainMenuService.getMainMenuMessage(chatId, newParagraph, profileData);
            }
        /*if (botState.equals(BotState.PLAY_SCENARIO)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askName");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        }*/

        //if (currentParagraph != newParagraph.getId() || currentParagraph == 1) {

        //}
        /*replyToUser = messagesService.getReplyMessage(chatId, newParagraph.getText());
        replyToUser.setReplyMarkup(getInlineMessageButtons(newParagraph));*/

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
