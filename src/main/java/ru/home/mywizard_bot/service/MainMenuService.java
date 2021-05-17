package ru.home.mywizard_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.Paragraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Управляет отображением главного меню в чате.
 *
 * @author Alex Tonkikh
 */
@Service
public class MainMenuService {

    public SendMessage getMainMenuMessage(final long chatId, final Paragraph paragraph) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(paragraph);
        final SendMessage mainMenuMessage = createMessageWithKeyboard(chatId, paragraph.getText(), replyKeyboardMarkup);
        return mainMenuMessage;
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard(Paragraph paragraph) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        List<Link> links = paragraph.getLinks();

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

    public SendMessage getMainMenuMessage(final long chatId, final String textMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
        final SendMessage mainMenuMessage = createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
        return mainMenuMessage;
    }

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

    private SendMessage createMessageWithKeyboard(final long chatId,
                                                  String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }
}
