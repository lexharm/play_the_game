package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.Paragraph;

public class ShowInventory implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        Paragraph paragraph = profileData.getCurrentMenu();
        paragraph.setText(profileData.toString());
    }
}
