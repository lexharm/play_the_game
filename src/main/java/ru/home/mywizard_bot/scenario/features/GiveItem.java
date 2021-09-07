package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Item;

public class GiveItem implements Feature {
    Item item;
    int count;

    public GiveItem() {}

    public GiveItem(Item item) {
        this.item = item;
        count = 1;
    }

    public GiveItem(Item item, int count) {
        this(item);
        this.count = count;
    }

    @Override
    public void engage(UserProfileData profileData) {
        for (int i = 0; i < count; i++) {
            profileData.addItem(item);
        }
    }
}
