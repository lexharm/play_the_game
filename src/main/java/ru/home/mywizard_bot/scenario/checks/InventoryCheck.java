package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Item;
import ru.home.mywizard_bot.utils.Condition;

import java.util.Map;

public class InventoryCheck extends Check {
    int count;
    Condition condition;

    public InventoryCheck(String value) {
        super(value);
    }

    public InventoryCheck(String value, boolean presence) {
        super(value, presence);
    }

    public InventoryCheck(Item item) {
        this(item.getId());
    }

    public InventoryCheck(Item item, int count, Condition condition) {
        this(item);
        this.count = count;
        this.condition = condition;
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
