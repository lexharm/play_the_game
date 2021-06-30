package ru.home.mywizard_bot.botapi.handlers.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.CallbackHandler;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.cache.UserDataCache;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.MainMenuService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MenuCallbackHandler implements CallbackHandler {
    private final UserDataCache userDataCache;
    private final MainMenuService mainMenuService;
    private final Story story;

    public MenuCallbackHandler(UserDataCache userDataCache, MainMenuService mainMenuService, Story story) {
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
        this.story = story;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message message) {
        log.info("MenuCallbackHandler User:{}, userId: {}, chatId: {}, with text: {}",
                message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), message.getText());
        String usersAnswer = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        UserProfileData profileData = userDataCache.getUserProfileData(userId);

        Paragraph currentMenu = profileData.getCurrentMenu(story);
        List<Link> links = new ArrayList<>();
        links.addAll(currentMenu.getLinks());
        links.addAll(story.getExtraLinks(BotState.SHOW_MAIN_MENU));

        Paragraph newParagraph = currentMenu;
        for (Link link : links) {
            if (usersAnswer.equals(link.getText())) {
                newParagraph = story.getMenuParagraph(link);
                link.engageFeatures(profileData);
                switch (profileData.getBotState()) {
                    case PLAY_SCENARIO:
                    case COMBAT:
                        newParagraph = profileData.getCurrentParagraph();
                        break;
                    default:
                        //SHOW_MAIN_MENU
                        profileData.setCurrentMenu(newParagraph);
                        break;
                }
                break;
            }
        }
        if ((!newParagraph.getId().equals(currentMenu.getId())) && (!newParagraph.getId().equals(profileData.getCurrentParagraph().getId()))) {
            newParagraph.engageFeatures(profileData);
        }
        userDataCache.saveUserProfileData(userId, profileData);

        SendMessage replyMessage = mainMenuService.getMainMenuMessage(chatId, newParagraph, profileData, story);

        //replyMessage.setReplyMarkup(getInlineMessageButtons());

        return replyMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}
