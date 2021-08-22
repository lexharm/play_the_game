package ru.home.mywizard_bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.*;
import ru.home.mywizard_bot.scenario.actions.Action;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Управляет отображением главного меню в чате.
 *
 * @author Alex Tonkikh
 */
@Service
@Slf4j
public class MainMenuService {
    private final ReplyMessagesService messagesService;

    public MainMenuService(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    public List<PartialBotApiMethod<?>> getMainMenuMessage(final long chatId, final Paragraph paragraph, UserProfileData profileData, Story story, boolean newMessage) {
        List<PartialBotApiMethod<?>> replyMessagesList = new ArrayList<>();

        //Step 0: get both keyboards: inline and reply
        InlineKeyboardMarkup inlineKeyboard = getInlineKeyboard(paragraph, profileData, story);
        ReplyKeyboardMarkup replyKeyboard = getReplyKeyboard(paragraph, profileData, story);

        //Step 1: delete existing inline keyboard
        if (newMessage && profileData.isHasInlineKeyboard()) {
            replyMessagesList.add(new EditMessageReplyMarkup()
                    .setChatId(chatId)
                    .setMessageId(profileData.getLastMessageId())
                    .setReplyMarkup(new InlineKeyboardMarkup()));
        }

        //Step 2: add illustration to reply
        Illustration illustration = paragraph.getIllustration();
        if (illustration != null) {
            try {
                File image = ResourceUtils.getFile("classpath:" + illustration.getImagePath());
                replyMessagesList.add(new SendPhoto().setChatId(chatId).setCaption(illustration.getCaption()).setPhoto(image));
            } catch (FileNotFoundException e) {
                log.info("There is no image file: {}", illustration.getImagePath());
            }
        }

        //Step 3: add paragraph texts
        for (int i = 0; i < paragraph.getTextsList().size(); i++) {
            if (i == 0 && !newMessage) {
                //!newMessage means that reply changes existing message
                replyMessagesList.add(new EditMessageText().setChatId(chatId).setMessageId(profileData.getLastMessageId()).setText(paragraph.getTextsList().get(i)).enableMarkdown(true));
                continue;
            }
            replyMessagesList.add(new SendMessage().setChatId(chatId).setText(paragraph.getTextsList().get(i)).enableMarkdown(true));
        }

        //Step 4: add additional methods
        if (replyKeyboard != null && inlineKeyboard != null) {
            if (replyMessagesList.size() == 1) {
                log.info("replyKeyboard != null && inlineKeyboard != null && size = 1");
                replyMessagesList.add(0, new SendMessage()
                        .setChatId(chatId)
                        .setReplyMarkup(replyKeyboard)
                        .enableMarkdown(true)
                        .setText(messagesService.getText("bot.reportUs")));
                replyMessagesList.add(1, new DeleteMessage(chatId, 0));
                BotApiMethod botApiMethod = (BotApiMethod) replyMessagesList.get(2);
                if (botApiMethod instanceof SendMessage) {
                    ((SendMessage) botApiMethod).setReplyMarkup(new InlineKeyboardMarkup());
                }
                replyMessagesList.add(new EditMessageReplyMarkup()
                        .setChatId(chatId)
                        .setReplyMarkup(inlineKeyboard));
            } else if (replyMessagesList.size() == 2) {
                log.info("replyKeyboard != null && inlineKeyboard != null && size = 2");
                PartialBotApiMethod botApiMethod = replyMessagesList.get(0);
                if (botApiMethod instanceof SendMessage) {
                    ((SendMessage) botApiMethod).setReplyMarkup(replyKeyboard).enableMarkdown(true);
                }
                BotApiMethod botApiMethod2 = (BotApiMethod) replyMessagesList.get(1);
                if (botApiMethod2 instanceof SendMessage) {
                    ((SendMessage) botApiMethod2).setReplyMarkup(new InlineKeyboardMarkup());
                }
                replyMessagesList.add(new EditMessageReplyMarkup()
                        .setChatId(chatId)
                        .setReplyMarkup(inlineKeyboard));
            } else {
                log.info("replyKeyboard != null && inlineKeyboard != null && size > 2");
                PartialBotApiMethod botApiMethod = replyMessagesList.get(0);
                //BotApiMethod botApiMethod = (BotApiMethod) replyMessagesList.get(0);
                if (botApiMethod instanceof SendMessage) {
                    ((SendMessage) botApiMethod).setReplyMarkup(new ReplyKeyboardRemove());
                }
                /*((SendMessage) replyMessagesList.get(replyMessagesList.size()-2))
                        .setReplyMarkup(replyKeyboard)
                        .enableMarkdown(true);
                ((SendMessage) replyMessagesList.get(replyMessagesList.size()-1)).setReplyMarkup(inlineKeyboard);*/
                BotApiMethod botApiMethod2 = (BotApiMethod) replyMessagesList.get(replyMessagesList.size()-1);
                if (botApiMethod2 instanceof SendMessage) {
                    ((SendMessage) botApiMethod2)
                            .setReplyMarkup(new InlineKeyboardMarkup())
                            .enableMarkdown(true);
                }
                replyMessagesList.add(new EditMessageReplyMarkup()
                        .setChatId(chatId)
                        .setReplyMarkup(inlineKeyboard));
            }
            profileData.setHasReplyKeyboard(true);
            profileData.setHasInlineKeyboard(true);
        } else if (replyKeyboard != null) {
            if (replyMessagesList.size() == 1) {
                log.info("replyKeyboard != null && size = 1");
                PartialBotApiMethod botApiMethod = replyMessagesList.get(0);
                if (botApiMethod instanceof SendMessage) {
                    ((SendMessage) botApiMethod).setReplyMarkup(replyKeyboard).enableMarkdown(true);
                }
            } else if (replyMessagesList.size() == 2) {
                log.info("replyKeyboard != null && size = 2");
                PartialBotApiMethod botApiMethod = replyMessagesList.get(0);
                if (botApiMethod instanceof SendMessage) {
                    ((SendMessage) botApiMethod).setReplyMarkup(new ReplyKeyboardRemove());
                }
                BotApiMethod botApiMethod2 = (BotApiMethod) replyMessagesList.get(1);
                if (botApiMethod2 instanceof SendMessage) {
                    ((SendMessage) botApiMethod2).setReplyMarkup(replyKeyboard).enableMarkdown(true);
                }
            } else {
                log.info("replyKeyboard != null && size > 2");
                PartialBotApiMethod botApiMethod = replyMessagesList.get(0);
                if (botApiMethod instanceof SendMessage) {
                    ((SendMessage) botApiMethod).setReplyMarkup(new ReplyKeyboardRemove());
                }
                BotApiMethod botApiMethod2 = (BotApiMethod) replyMessagesList.get(replyMessagesList.size()-1);
                if (botApiMethod2 instanceof SendMessage) {
                    ((SendMessage) botApiMethod2)
                            .setReplyMarkup(replyKeyboard)
                            .enableMarkdown(true);
                }
            }
            profileData.setHasReplyKeyboard(true);
            profileData.setHasInlineKeyboard(false);
        } else if (inlineKeyboard != null) {
            if (replyMessagesList.size() == 1) {
                log.info("inlineKeyboard != null && size = 1");
                int i = 0;
                if (profileData.isHasReplyKeyboard()) {
                    replyMessagesList.add(i++, new SendMessage()
                            .setChatId(chatId)
                            .setReplyMarkup(new ReplyKeyboardRemove())
                            .enableMarkdown(true)
                            .setText(messagesService.getText("bot.reportUs")));
                    replyMessagesList.add(i++, new DeleteMessage(chatId, 0));
                }
                //((SendMessage) replyMessagesList.get(2)).setReplyMarkup(new InlineKeyboardMarkup());
                if (replyMessagesList.get(i) instanceof SendMessage) {
                    ((SendMessage) replyMessagesList.get(i)).setReplyMarkup(new InlineKeyboardMarkup());
                } else if (replyMessagesList.get(i) instanceof EditMessageReplyMarkup) {
                    ((EditMessageText) replyMessagesList.get(i)).setReplyMarkup(new InlineKeyboardMarkup());
                }
                replyMessagesList.add(new EditMessageReplyMarkup()
                        .setChatId(chatId)
                        .setReplyMarkup(inlineKeyboard)
                        .setMessageId(profileData.getLastMessageId()));
            } else if (replyMessagesList.size() == 2) {
                log.info("inlineKeyboard != null && size = 2");
                if (profileData.isHasReplyKeyboard()) {
                    SendMessage firstMessage = (SendMessage) replyMessagesList.get(0);
                    firstMessage.setReplyMarkup(new ReplyKeyboardRemove());
                }
                SendMessage lastMessage = (SendMessage) replyMessagesList.get(1);
                lastMessage.setReplyMarkup(new InlineKeyboardMarkup());
                replyMessagesList.add(new EditMessageReplyMarkup()
                        .setChatId(chatId)
                        .setReplyMarkup(inlineKeyboard)
                        .setMessageId(profileData.getLastMessageId()));
            } else {
                log.info("inlineKeyboard != null && size > 2");
                if (profileData.isHasReplyKeyboard()) {
                    SendMessage firstMessage = replyMessagesList.stream().filter(SendMessage.class::isInstance).findFirst().map(SendMessage.class::cast).get();
                    firstMessage.setReplyMarkup(new ReplyKeyboardRemove());
                }
                SendMessage lastMessage = (SendMessage) replyMessagesList.get(replyMessagesList.size()-1);
                lastMessage.setReplyMarkup(inlineKeyboard);
            }
            profileData.setHasReplyKeyboard(false);
            profileData.setHasInlineKeyboard(true);
        }
        return replyMessagesList;
    }

    public List<PartialBotApiMethod<?>> getIllegalActionMessage(CallbackQuery callbackQuery) {
        List<PartialBotApiMethod<?>> replyMessagesList = new ArrayList<>();
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setText(messagesService.getText("bot.illegalAction"));
        replyMessagesList.add(answerCallbackQuery);
        return replyMessagesList;
    }

    public List<PartialBotApiMethod<?>> getIllegalActionMessage(String callbackQueryId) {
        List<PartialBotApiMethod<?>> replyMessagesList = new ArrayList<>();
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQueryId);
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setText(messagesService.getText("bot.illegalAction"));
        replyMessagesList.add(answerCallbackQuery);
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
    /*@Deprecated
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
    }*/

    private InlineKeyboardMarkup getInlineKeyboard(Paragraph paragraph, UserProfileData profileData, Story story) {
        InlineKeyboardMarkup inlineKeyboardMarkup = null;
        List<List<InlineKeyboardButton>> inlineKeyboard = new ArrayList<>();
        for (Action link : paragraph.getInlineLinks1()) {
            if (link.test(profileData)) {
                InlineKeyboardButton button = new InlineKeyboardButton().setText(link.getCaption());
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
        for (Action link : paragraph.getMovementLinks()) {
            if (link.test(profileData)) {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(link.getCaption()));
                keyboard.add(row);
            }
        }
        /*KeyboardRow extraRow = new KeyboardRow();
        for (Link link : story.getExtraLinks().get(profileData.getBotState())) {
            extraRow.add(new KeyboardButton(link.getText()));
        }
        if (extraRow.size() > 0) {
            keyboard.add(extraRow);
        }*/
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

    /*@Deprecated
    public SendMessage getMainMenuMessageForCombat(final long chatId, final String textMessage, final Paragraph paragraph, Enemy enemy, int playerStrength) {
        String text = textMessage + " " + enemy.toString() + " Ваше здоровье " + playerStrength;
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboardCombat(paragraph);
        final SendMessage mainMenuMessage = createMessageWithKeyboard(chatId, text, replyKeyboardMarkup);
        return mainMenuMessage;
    }*/

    /*@Deprecated
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
    }*/
}
