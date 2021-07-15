package ru.home.mywizard_bot.botapi;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

/**
 * Обработчик сообщений
 */
public interface InputMessageHandler {
    //SendMessage handle(Message message);
    List<PartialBotApiMethod<?>> handle(Message message);

    BotState getHandlerName();
}
