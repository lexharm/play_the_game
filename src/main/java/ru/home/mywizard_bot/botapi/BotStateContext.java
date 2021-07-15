package ru.home.mywizard_bot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines message handlers for each state.
 */
@Component
public class BotStateContext {
    private Map<BotState, InputMessageHandler> messageHandlers = new HashMap<>();
    private Map<BotState, CallbackHandler> callbackHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers, List<CallbackHandler> callbackHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
        callbackHandlers.forEach(handler -> this.callbackHandlers.put(handler.getHandlerName(), handler));
    }

    public List<PartialBotApiMethod<?>> processCallbackQuery(BotState currentState, CallbackQuery callbackQuery) {
        CallbackHandler currentMessageHandler = findCallbackHandler(currentState);
        return currentMessageHandler.handle(callbackQuery);
    }

    private CallbackHandler findCallbackHandler(BotState currentState) {
        return callbackHandlers.get(currentState);
    }

    /*@Deprecated
    public SendMessage processInputMessage_old(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }*/

    public List<PartialBotApiMethod<?>> processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private InputMessageHandler findMessageHandler(BotState currentState) {
        return messageHandlers.get(currentState);
    }
}
