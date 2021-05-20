package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;

public interface Feature {
    void engage(UserProfileData profileData);
}
