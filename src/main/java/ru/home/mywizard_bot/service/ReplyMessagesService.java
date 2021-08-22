package ru.home.mywizard_bot.service;

import org.springframework.stereotype.Service;


/**
 * Формирует готовые ответные сообщения в чат.
 */
@Service
public class ReplyMessagesService {

    private LocaleMessageService localeMessageService;

    public ReplyMessagesService(LocaleMessageService messageService) {
        this.localeMessageService = messageService;
    }

    public String getText(String replyText) {
        return localeMessageService.getMessage(replyText);
    }


}
