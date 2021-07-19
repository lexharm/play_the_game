package ru.home.mywizard_bot.botapi;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.mywizard_bot.botapi.BotState;

import java.util.List;

public interface CallbackHandler {
    List<PartialBotApiMethod<?>> handle(CallbackQuery callbackQuery);

    BotState getHandlerName();
}
