package ru.home.mywizard_bot;


import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.home.mywizard_bot.botapi.TelegramFacade;

import java.util.List;


public class MyWizardTelegramBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;
    private TelegramFacade telegramFacade;

    public MyWizardTelegramBot(DefaultBotOptions botOptions, TelegramFacade telegramFacade) {
        super(botOptions);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        /*final BotApiMethod<?> replyMessageToUser = telegramFacade.handleUpdate(update);
        return replyMessageToUser;*/
        List<BotApiMethod<?>> replyMessagesList = telegramFacade.handleUpdate(update);
        int i = 0;
        if (replyMessagesList.size() > 1) {
            do {
                try {
                    execute(replyMessagesList.get(i++));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } while (i < replyMessagesList.size() - 1);
        }

        return replyMessagesList.get(i);
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
