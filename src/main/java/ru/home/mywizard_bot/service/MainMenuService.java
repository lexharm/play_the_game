package ru.home.mywizard_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.Enemy;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Управляет отображением главного меню в чате.
 *
 * @author Alex Tonkikh
 */
@Service
public class MainMenuService {

    public List<BotApiMethod<?>> getMainMenuMessage(final long chatId, final Paragraph paragraph, UserProfileData profileData, Story story) {
        //final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(paragraph, profileData, story);
        List<BotApiMethod<?>> replyMessagesList = new ArrayList<>();
        //final ReplyKeyboard replyKeyboard = getKeyboard(paragraph, profileData, story);
        InlineKeyboardMarkup inlineKeyboard = getInlineKeyboard(paragraph, profileData, story);
        ReplyKeyboardMarkup replyKeyboard = getReplyKeyboard(paragraph, profileData, story);
        /*String messageText = "";
        if (profileData.getMessage().length() > 0) {
            messageText = profileData.getMessage() + "\n";
        }
        messageText += paragraph.getText();*/
        for (String text : paragraph.getTextsList()) {
            replyMessagesList.add(new SendMessage().setChatId(chatId).setText(text));
        }

        if (replyKeyboard != null && inlineKeyboard != null) {
            if (replyMessagesList.size() == 1) {
                SendMessage message = (SendMessage) replyMessagesList.get(0);
                message.setReplyMarkup(inlineKeyboard);
                //SendMessage dummyMessage = new SendMessage().setChatId(chatId).setReplyMarkup(replyKeyboard).enableMarkdown(true).setText("Both");
                SendMessage dummyMessage = new SendMessage().setChatId(chatId).setReplyMarkup(new ReplyKeyboardRemove()).enableMarkdown(true).setText("Both");
                replyMessagesList.add(0, dummyMessage);
                DeleteMessage deleteMessage = new DeleteMessage(chatId, 0);
                replyMessagesList.add(1, deleteMessage);
            } else if (replyMessagesList.size() == 2) {
                SendMessage firstMessage = (SendMessage) replyMessagesList.get(0);
                firstMessage.setReplyMarkup(replyKeyboard).enableMarkdown(true);
                SendMessage lastMessage = (SendMessage) replyMessagesList.get(1);
                lastMessage.setReplyMarkup(inlineKeyboard);
            } else {
                SendMessage firstMessage = (SendMessage) replyMessagesList.get(0);
                firstMessage.setReplyMarkup(new ReplyKeyboardRemove());
                SendMessage penultMessage = (SendMessage) replyMessagesList.get(replyMessagesList.size()-2);
                penultMessage.setReplyMarkup(replyKeyboard).enableMarkdown(true);
                SendMessage lastMessage = (SendMessage) replyMessagesList.get(replyMessagesList.size()-1);
                lastMessage.setReplyMarkup(inlineKeyboard);
            }
        } else if (replyKeyboard != null) {
            if (replyMessagesList.size() == 1) {
                SendMessage message = (SendMessage) replyMessagesList.get(0);
                message.setReplyMarkup(replyKeyboard);
            } else if (replyMessagesList.size() == 2) {
                SendMessage firstMessage = (SendMessage) replyMessagesList.get(0);
                firstMessage.setReplyMarkup(new ReplyKeyboardRemove());
                SendMessage lastMessage = (SendMessage) replyMessagesList.get(1);
                lastMessage.setReplyMarkup(replyKeyboard);
            } else {
                SendMessage firstMessage = (SendMessage) replyMessagesList.get(0);
                firstMessage.setReplyMarkup(new ReplyKeyboardRemove());
                SendMessage lastMessage = (SendMessage) replyMessagesList.get(replyMessagesList.size()-1);
                lastMessage.setReplyMarkup(replyKeyboard);
            }
        } else if (inlineKeyboard != null) {
            if (replyMessagesList.size() == 1) {
                SendMessage message = (SendMessage) replyMessagesList.get(0);
                message.setReplyMarkup(inlineKeyboard);
                SendMessage dummyMessage = new SendMessage().setChatId(chatId).setReplyMarkup(new ReplyKeyboardRemove()).setText("Inline");
                replyMessagesList.add(0, dummyMessage);
            } else if (replyMessagesList.size() == 2) {
                SendMessage firstMessage = (SendMessage) replyMessagesList.get(0);
                firstMessage.setReplyMarkup(new ReplyKeyboardRemove());
                SendMessage lastMessage = (SendMessage) replyMessagesList.get(1);
                lastMessage.setReplyMarkup(inlineKeyboard);
            } else {
                SendMessage firstMessage = (SendMessage) replyMessagesList.get(0);
                firstMessage.setReplyMarkup(new ReplyKeyboardRemove());
                SendMessage lastMessage = (SendMessage) replyMessagesList.get(replyMessagesList.size()-1);
                lastMessage.setReplyMarkup(inlineKeyboard);
            }
        }

        //final SendMessage mainMenuMessage = createMessageWithKeyboard(chatId, messageText, replyKeyboard);
        //profileData.setMessage("");
        /*SendMessage lastMessage = (SendMessage) replyMessagesList.get(replyMessagesList.size()-1);
        lastMessage.enableMarkdown(true);
        lastMessage.setReplyMarkup(replyKeyboard);*/
        return replyMessagesList;
    }

    /*private ReplyKeyboardMarkup getMainMenuKeyboard(Paragraph paragraph, UserProfileData profileData, Story story) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        List<Link> links = paragraph.getLinks();
        for (Link link : links) {
            if (link.test(profileData)) {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(link.getText()));
                keyboard.add(row);
            }
        }
        KeyboardRow row1 = new KeyboardRow();
        for (Link link : story.getExtraLinks().get(profileData.getBotState())) {
            row1.add(new KeyboardButton(link.getText()));
        }
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }*/
    @Deprecated
    private ReplyKeyboard getKeyboard(Paragraph paragraph, UserProfileData profileData, Story story) {
        List<List<InlineKeyboardButton>> inlineKeyboard = new ArrayList<>();
        for (Link link : paragraph.getInlineLinks()) {
            if (link.test(profileData)) {
                InlineKeyboardButton button = new InlineKeyboardButton().setText(link.getText());
                button.setCallbackData(link.getId());
                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                buttonList.add(button);
                inlineKeyboard.add(buttonList);
            }
        }
        if (inlineKeyboard.size() > 0) {
            InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(inlineKeyboard);
            return inlineKeyboardMarkup;
        }
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (Link link : paragraph.getLinks()) {
            if (link.test(profileData)) {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(link.getText()));
                keyboard.add(row);
            }
        }
        KeyboardRow extraRow = new KeyboardRow();
        for (Link link : story.getExtraLinks().get(profileData.getBotState())) {
            extraRow.add(new KeyboardButton(link.getText()));
        }
        keyboard.add(extraRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private InlineKeyboardMarkup getInlineKeyboard(Paragraph paragraph, UserProfileData profileData, Story story) {
        InlineKeyboardMarkup inlineKeyboardMarkup = null;
        List<List<InlineKeyboardButton>> inlineKeyboard = new ArrayList<>();
        for (Link link : paragraph.getInlineLinks()) {
            if (link.test(profileData)) {
                InlineKeyboardButton button = new InlineKeyboardButton().setText(link.getText());
                button.setCallbackData(link.getId());
                List<InlineKeyboardButton> buttonList = new ArrayList<>();
                buttonList.add(button);
                inlineKeyboard.add(buttonList);
            }
        }
        if (inlineKeyboard.size() > 0) {
            inlineKeyboardMarkup = new InlineKeyboardMarkup();
            inlineKeyboardMarkup.setKeyboard(inlineKeyboard);
        }
        return inlineKeyboardMarkup;
    }

    private ReplyKeyboardMarkup getReplyKeyboard(Paragraph paragraph, UserProfileData profileData, Story story) {
        ReplyKeyboardMarkup replyKeyboardMarkup = null;
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (Link link : paragraph.getLinks()) {
            if (link.test(profileData)) {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(link.getText()));
                keyboard.add(row);
            }
        }
        KeyboardRow extraRow = new KeyboardRow();
        for (Link link : story.getExtraLinks().get(profileData.getBotState())) {
            extraRow.add(new KeyboardButton(link.getText()));
        }
        keyboard.add(extraRow);
        if (keyboard.size() > 0) {
            replyKeyboardMarkup = new ReplyKeyboardMarkup();
            replyKeyboardMarkup.setSelective(true);
            replyKeyboardMarkup.setResizeKeyboard(true);
            replyKeyboardMarkup.setOneTimeKeyboard(false);
            replyKeyboardMarkup.setKeyboard(keyboard);
        }
        return replyKeyboardMarkup;
    }

    private SendMessage createMessageWithKeyboard(final long chatId, String textMessage, final ReplyKeyboard replyKeyboard) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(textMessage);
        if (replyKeyboard != null) {
            sendMessage.setReplyMarkup(replyKeyboard);
        }
        return sendMessage;
    }

    @Deprecated
    public SendMessage getMainMenuMessage(final long chatId, final String textMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
        final SendMessage mainMenuMessage = createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
        return mainMenuMessage;
    }
    @Deprecated
    private ReplyKeyboardMarkup getMainMenuKeyboard() {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton("Новая игра"));
        row2.add(new KeyboardButton("Поддержать"));
        row3.add(new KeyboardButton("Назад"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    @Deprecated
    public SendMessage getMainMenuMessageForCombat(final long chatId, final String textMessage, final Paragraph paragraph, Enemy enemy, int playerStrength) {
        String text = textMessage + " " + enemy.toString() + " Ваше здоровье " + playerStrength;
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboardCombat(paragraph);
        final SendMessage mainMenuMessage = createMessageWithKeyboard(chatId, text, replyKeyboardMarkup);
        return mainMenuMessage;
    }

    @Deprecated
    private ReplyKeyboardMarkup getMainMenuKeyboardCombat(Paragraph paragraph) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        List<Link> links = paragraph.getLinks();
        KeyboardRow row0 = new KeyboardRow();
        row0.add(new KeyboardButton("Н-на нах ёпта!"));
        keyboard.add(row0);
        for (Link link : links) {
            KeyboardRow row = new KeyboardRow();
            row.add(new KeyboardButton(link.getText()));
            keyboard.add(row);
        }
        KeyboardRow row1 = new KeyboardRow();
//        KeyboardRow row2 = new KeyboardRow();
//        KeyboardRow row3 = new KeyboardRow();
        row1.add(new KeyboardButton("Листок путешественника"));
        row1.add(new KeyboardButton("Меню"));
//        row3.add(new KeyboardButton("Помощь"));
        keyboard.add(row1);
//        keyboard.add(row2);
//        keyboard.add(row3);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }
}
