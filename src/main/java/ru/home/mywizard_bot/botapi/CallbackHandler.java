package ru.home.mywizard_bot.botapi;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.mywizard_bot.botapi.BotState;

public interface CallbackHandler {
    SendMessage handle(Message message);

    BotState getHandlerName();
}
