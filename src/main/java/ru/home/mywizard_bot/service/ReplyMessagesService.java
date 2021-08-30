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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Illustration;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.scenario.actions.Action;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


/**
 * Формирует ответные сообщения.
 * @author Alex Tonkikh
 */
@Slf4j
@Service
public class ReplyMessagesService {

    private final LocaleMessageService localeMessageService;

    public ReplyMessagesService(LocaleMessageService messageService) {
        this.localeMessageService = messageService;
    }

    public String getText(String replyText) {
        return localeMessageService.getMessage(replyText);
    }

    public List<PartialBotApiMethod<?>> getMainMenuMessage(long chatId, UserProfileData profileData, Story story, boolean newMessage) {
        List<PartialBotApiMethod<?>> replyMessagesList = new ArrayList<>();

        Paragraph paragraph = profileData.getNewParagraph();

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

        //Step 2-1: Add status
        String additionalStatus = profileData.getCombatStatus();
        if (additionalStatus.length() > 0) {
            replyMessagesList.add(new SendMessage().setChatId(chatId).setText(additionalStatus));
            profileData.setCombatStatus("");
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
                //TODO: what if 1st message is edit method?
                BotApiMethod botApiMethod = (BotApiMethod) replyMessagesList.get(0);
                if (botApiMethod instanceof SendMessage) {
                    ((SendMessage) botApiMethod).setReplyMarkup(inlineKeyboard);
                }
                /*replyMessagesList.add(new EditMessageReplyMarkup()
                        .setChatId(chatId)
                        .setReplyMarkup(inlineKeyboard));*/
                if (!profileData.equalsReplyKeyboard(replyKeyboard)) {
                    replyMessagesList.add(0, new SendMessage()
                            .setChatId(chatId)
                            .setReplyMarkup(replyKeyboard)
                            .enableMarkdown(true)
                            .setText(getText("bot.reportUs")));
                    replyMessagesList.add(1, new DeleteMessage(chatId, 0));
                }
            } else if (replyMessagesList.size() == 2) {
                log.info("replyKeyboard != null && inlineKeyboard != null && size = 2");
                //TODO: what if 1st message is photo? we have to put reply keyboard into dummy message?
                PartialBotApiMethod botApiMethod = replyMessagesList.get(0);
                if (botApiMethod instanceof SendMessage) {
                    ((SendMessage) botApiMethod).setReplyMarkup(replyKeyboard).enableMarkdown(true);
                }
                BotApiMethod botApiMethod2 = (BotApiMethod) replyMessagesList.get(1);
                if (botApiMethod2 instanceof SendMessage) {
                    ((SendMessage) botApiMethod2).setReplyMarkup(inlineKeyboard);
                }
                /*replyMessagesList.add(new EditMessageReplyMarkup()
                        .setChatId(chatId)
                        .setReplyMarkup(inlineKeyboard));*/
            } else {
                log.info("replyKeyboard != null && inlineKeyboard != null && size > 2");
                //TODO: what if 1st message is photo? we have to delete keyboard into dummy message?
                PartialBotApiMethod botApiMethod = replyMessagesList.get(0);
                if (botApiMethod instanceof SendMessage) {
                    if (!profileData.equalsReplyKeyboard(replyKeyboard)) {
                        ((SendMessage) botApiMethod).setReplyMarkup(new ReplyKeyboardRemove());
                    }
                }
                BotApiMethod botApiMethod2 = (BotApiMethod) replyMessagesList.get(replyMessagesList.size()-1);
                if (botApiMethod2 instanceof SendMessage) {
                    ((SendMessage) botApiMethod2)
                            .setReplyMarkup(inlineKeyboard)
                            .enableMarkdown(true);
                }
                /*replyMessagesList.add(new EditMessageReplyMarkup()
                        .setChatId(chatId)
                        .setReplyMarkup(inlineKeyboard));*/
            }
            profileData.setReplyKeyboardHash(replyKeyboard.hashCode());
            profileData.setInlineKeyboardHash(inlineKeyboard.hashCode());
        } else if (replyKeyboard != null) {
            if (replyMessagesList.size() == 1) {
                log.info("replyKeyboard != null && size = 1");
                if (!profileData.equalsReplyKeyboard(replyKeyboard)) {
                    PartialBotApiMethod botApiMethod = replyMessagesList.get(0);
                    //TODO: what if 1st message is edit method?
                    if (botApiMethod instanceof SendMessage) {
                        ((SendMessage) botApiMethod).setReplyMarkup(replyKeyboard).enableMarkdown(true);
                    }
                }
            } else if (replyMessagesList.size() == 2) {
                log.info("replyKeyboard != null && size = 2");
                //TODO: what if 1st message is photo? we have to put reply keyboard into dummy message?
                if (!profileData.equalsReplyKeyboard(replyKeyboard)) {
                    PartialBotApiMethod botApiMethod = replyMessagesList.get(0);
                    if (botApiMethod instanceof SendMessage) {
                        //we delete keyboard cuz we will probably have delay in sending messages
                        ((SendMessage) botApiMethod).setReplyMarkup(new ReplyKeyboardRemove());
                    }
                    BotApiMethod botApiMethod2 = (BotApiMethod) replyMessagesList.get(1);
                    if (botApiMethod2 instanceof SendMessage) {
                        ((SendMessage) botApiMethod2).setReplyMarkup(replyKeyboard).enableMarkdown(true);
                    }
                }
            } else {
                log.info("replyKeyboard != null && size > 2");
                if (!profileData.equalsReplyKeyboard(replyKeyboard)) {
                    PartialBotApiMethod botApiMethod = replyMessagesList.get(0);
                    if (botApiMethod instanceof SendMessage) {
                        ((SendMessage) botApiMethod).setReplyMarkup(new ReplyKeyboardRemove());
                    }
                    BotApiMethod botApiMethod2 = (BotApiMethod) replyMessagesList.get(replyMessagesList.size() - 1);
                    if (botApiMethod2 instanceof SendMessage) {
                        ((SendMessage) botApiMethod2)
                                .setReplyMarkup(replyKeyboard)
                                .enableMarkdown(true);
                    }
                }
            }
            profileData.setReplyKeyboardHash(replyKeyboard.hashCode());
            profileData.setInlineKeyboardHash(null);
        } else if (inlineKeyboard != null) {
            if (replyMessagesList.size() == 1) {
                log.info("inlineKeyboard != null && size = 1");
                int i = 0;
                if (profileData.isHasReplyKeyboard()) {
                    replyMessagesList.add(i++, new SendMessage()
                            .setChatId(chatId)
                            .setReplyMarkup(new ReplyKeyboardRemove())
                            .enableMarkdown(true)
                            .setText(getText("bot.reportUs")));
                    replyMessagesList.add(i++, new DeleteMessage(chatId, 0));
                }
                if (replyMessagesList.get(i) instanceof SendMessage) {
                    ((SendMessage) replyMessagesList.get(i)).setReplyMarkup(inlineKeyboard);
                } else if (replyMessagesList.get(i) instanceof EditMessageReplyMarkup) {
                    ((EditMessageReplyMarkup) replyMessagesList.get(i)).setReplyMarkup(inlineKeyboard);
                } else if (replyMessagesList.get(i) instanceof EditMessageText) {
                    ((EditMessageText) replyMessagesList.get(i)).setReplyMarkup(inlineKeyboard);
                }
                /*replyMessagesList.add(new EditMessageReplyMarkup()
                        .setChatId(chatId)
                        .setReplyMarkup(inlineKeyboard)
                        .setMessageId(profileData.getLastMessageId()));*/
            } else if (replyMessagesList.size() == 2) {
                log.info("inlineKeyboard != null && size = 2");
                if (profileData.isHasReplyKeyboard()) {
                    //TODO: 1st message can be of another type
                    SendMessage firstMessage = (SendMessage) replyMessagesList.get(0);
                    firstMessage.setReplyMarkup(new ReplyKeyboardRemove());
                }
                SendMessage lastMessage = (SendMessage) replyMessagesList.get(1);
                lastMessage.setReplyMarkup(inlineKeyboard);
                /*replyMessagesList.add(new EditMessageReplyMarkup()
                        .setChatId(chatId)
                        .setReplyMarkup(inlineKeyboard)
                        .setMessageId(profileData.getLastMessageId()));*/
            } else {
                log.info("inlineKeyboard != null && size > 2");
                if (profileData.isHasReplyKeyboard()) {
                    SendMessage firstMessage = replyMessagesList.stream().filter(SendMessage.class::isInstance).findFirst().map(SendMessage.class::cast).get();
                    firstMessage.setReplyMarkup(new ReplyKeyboardRemove());
                }
                SendMessage lastMessage = (SendMessage) replyMessagesList.get(replyMessagesList.size()-1);
                lastMessage.setReplyMarkup(inlineKeyboard);
            }
            profileData.setReplyKeyboardHash(null);
            profileData.setInlineKeyboardHash(inlineKeyboard.hashCode());
        }
        return replyMessagesList;
    }

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
        //TODO: add extra links
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

    public List<PartialBotApiMethod<?>> getIllegalActionMessage(String callbackQueryId) {
        List<PartialBotApiMethod<?>> replyMessagesList = new ArrayList<>();
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQueryId);
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setText(getText("bot.illegalAction"));
        replyMessagesList.add(answerCallbackQuery);
        return replyMessagesList;
    }

    public List<PartialBotApiMethod<?>> getIllegalActionMessage(CallbackQuery callbackQuery) {
        List<PartialBotApiMethod<?>> replyMessagesList = new ArrayList<>();
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackQuery.getId());
        answerCallbackQuery.setShowAlert(true);
        answerCallbackQuery.setText(getText("bot.illegalAction"));
        replyMessagesList.add(answerCallbackQuery);
        return replyMessagesList;
    }
}
