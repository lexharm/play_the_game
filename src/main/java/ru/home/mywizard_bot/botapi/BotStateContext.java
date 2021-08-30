package ru.home.mywizard_bot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import ru.home.mywizard_bot.botapi.handlers.Handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines message handlers for each state.
 */
@Component
public class BotStateContext {
    private final Map<BotState, Handler> messageHandlers = new HashMap<>();

    public BotStateContext(List<Handler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public List<PartialBotApiMethod<?>> processInputMessage(BotState currentState, BotApiObject message) {
        Handler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }

    private Handler findMessageHandler(BotState currentState) {
        return messageHandlers.get(currentState);
    }
}
