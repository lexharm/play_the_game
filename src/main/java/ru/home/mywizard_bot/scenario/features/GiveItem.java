package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.Item;

public class GiveItem implements Feature {
    Item item;

    public GiveItem(Item item) {
        this.item = item;
    }

    @Override
    public void engage(UserProfileData profileData) {
        profileData.addItem(item);
    }
}
