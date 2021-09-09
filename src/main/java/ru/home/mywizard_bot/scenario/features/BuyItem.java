package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Item;

public class BuyItem implements Feature {
    Item item;
    int count;
    int cost;

    public BuyItem() {}

    public BuyItem(Item item, int cost) {
        this.item = item;
        this.cost = cost;
        count = 1;
    }

    public BuyItem(Item item, int cost, int count) {
        this(item, cost);
        this.count = count;
    }

    @Override
    public void engage(UserProfileData profileData) {
        for (int i = 0; i < count; i++) {
            profileData.addItem(item);
        }
    }
}
