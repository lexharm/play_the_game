package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;

import java.util.Map;

public class InventoryCheck extends Check {

    public InventoryCheck(String value) {
        super(value);
    }

    public InventoryCheck(String value, boolean presence) {
        super(value, presence);
    }

    @Override
    public boolean test(UserProfileData profileData) {
        for (Map.Entry<String, Integer> entry : profileData.getInventory().entrySet()) {
            if (entry.getKey().equals(value))
                return true;
        }
        return false;
    }
}
