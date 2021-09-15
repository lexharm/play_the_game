package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Item;
import ru.home.mywizard_bot.utils.Condition;

import java.util.Map;

public class InventoryCheck extends Check {
    int count;
    Condition condition;
    String value;
    boolean presence;

    public InventoryCheck() {}

    public InventoryCheck(String value) {
        this.value = value;
        presence = true;
    }

    public InventoryCheck(String value, boolean presence) {
        this(value);
        this.presence = presence;
    }

    public InventoryCheck(Item item) {
        this(item.getId());
    }

    public InventoryCheck(Item item, int count, Condition condition) {
        this(item);
        this.count = count;
        this.condition = condition;
    }

    public InventoryCheck(Item item, boolean presence) {
        this(item);
        if (presence) {
           this.count = 1;
           this.condition = Condition.MORE_EQUAL;
       } else {
            this.count = 0;
            this.condition = Condition.EQUAL;
       }
    }

    @Override
    public boolean test(UserProfileData profileData) {
        int itemCount = 0;
        for (Map.Entry<String, Integer> entry : profileData.getInventory().entrySet()) {
            if (entry.getKey().equals(value)) {
                itemCount = entry.getValue();
                break;
            }
        }
        switch (condition) {
            case EQUAL: return itemCount == count;
            case NOT_EQUAL: return itemCount != count;
            case MORE: return itemCount > count;
            case LESS: return itemCount < count;
            case MORE_EQUAL: return itemCount >= count;
            case LESS_EQUAL: return itemCount <= count;
        }
        return false;
    }
}
