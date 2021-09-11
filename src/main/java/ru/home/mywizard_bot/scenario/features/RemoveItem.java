package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Item;

public class RemoveItem implements Feature {
    Item item;
    int count;

    public RemoveItem() {}

    public RemoveItem(Item item) {
        this.item = item;
        count = 1;
    }

    public RemoveItem(Item item,int count) {
        this(item);
        this.count = count;
    }

    @Override
    public void engage(UserProfileData profileData) {
        for (int i = 0; i < count; i++) {
            profileData.removeItem(item);
        }
    }
}
