package ru.home.mywizard_bot.service;

import org.springframework.stereotype.Service;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.repository.UsersProfileMongoRepository;

import java.util.List;

@Service
public class UsersProfileDataService {
    private UsersProfileMongoRepository profileMongoRepository;

    public UsersProfileDataService(UsersProfileMongoRepository profileMongoRepository) {
        this.profileMongoRepository = profileMongoRepository;
    }

    public List<UserProfileData> getAllProfiles() {
        return profileMongoRepository.findAll();
    }

    public void saveUserProfileData(UserProfileData userProfileData) {
        profileMongoRepository.save(userProfileData);
    }

    public void deleteUsersProfileData(String profileDataId) {
        profileMongoRepository.deleteById(profileDataId);
    }

    public UserProfileData getUserProfileData(long chatId) {
        return profileMongoRepository.findByChatId(chatId);
    }

    public void deleteByChatId(long chatId) {
        profileMongoRepository.deleteByChatId(chatId);
    }

    public BotState getUserBotState(long chatId) {
        BotState botState = profileMongoRepository.findByChatId(chatId).getBotState();
        if (botState == null) {
            botState = BotState.SHOW_MAIN_MENU;
        }
        return botState;
    }

    public void setLastMessageId(long chatId, Integer lastMessageId) {
        UserProfileData profileData = profileMongoRepository.findByChatId(chatId);
        profileData.setLastMessageId(lastMessageId);
        saveUserProfileData(profileData);
    }
}
