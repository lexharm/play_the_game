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
    public List<BotApiMethod<?>> handle(CallbackQuery callbackQuery) {
        return processUsersInput(callbackQuery);
    }

    private List<BotApiMethod<?>> processUsersInput(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        log.info("MenuCallbackHandler User:{}, userId: {}, chatId: {}, with text: {}",
                message.getFrom().getUserName(), message.getFrom().getId(), message.getChatId(), callbackQuery.getData());
        String usersAnswer = callbackQuery.getData();
        int userId = callbackQuery.getFrom().getId();
        long chatId = message.getChatId();
        UserProfileData profileData = userDataCache.getUserProfileData(userId);

        Paragraph currentMenu = profileData.getCurrentMenu(story);
        List<Link> links = new ArrayList<>();
        links.addAll(currentMenu.getInlineLinks());
        //links.addAll(story.getExtraLinks(BotState.SHOW_MAIN_MENU));

        Paragraph newParagraph = currentMenu;
        boolean isParagraphChanged = false;
        Link matchedLink = null;
        for (Link link : links) {
            if (usersAnswer.equals(link.getId())) {
                matchedLink = link;
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
                isParagraphChanged = true;
                break;
            }
        }
        if (isParagraphChanged && (!newParagraph.getId().equals(currentMenu.getId())) && (!newParagraph.getId().equals(profileData.getCurrentParagraph().getId()))) {
            newParagraph.engageFeatures(profileData);
        }
        userDataCache.saveUserProfileData(userId, profileData);

        //replyMessage.setReplyMarkup(getInlineMessageButtons());
        boolean newMessage = matchedLink == null || matchedLink.isNewMessage();
        if (isParagraphChanged)
            return mainMenuService.getMainMenuMessage(chatId, newParagraph, profileData, story, newMessage);
        else
            return mainMenuService.getIllegalActionMessage(callbackQuery);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_MAIN_MENU;
    }
}
