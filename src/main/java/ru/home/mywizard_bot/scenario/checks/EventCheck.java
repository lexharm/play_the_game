package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.Item;

import java.util.Map;

public class EventCheck extends Check {

    public EventCheck(String value) {
        super(value);
    }

    public EventCheck(String value, boolean presence) {
        super(value, presence);
    }

    @Override
    public boolean test(UserProfileData profileData) {
        boolean hasCheck = false;
        for (Map.Entry<String, Integer> entry : profileData.getChecks().entrySet()) {
            if (entry.getKey().equals(value)) {
                hasCheck = true;
                break;
            }
        }
        if ((presence && hasCheck) || (!presence && !hasCheck))
            return true;
        else
            return false;
    }
}
