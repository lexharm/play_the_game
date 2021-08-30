package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;

public abstract class Check {
    String value;
    boolean presence;

    public Check() {}

    public Check(String value) {
        this.value = value;
        presence = true;
    }

    public Check(String value, boolean presence) {
        this(value);
        this.presence = presence;
    }

    public abstract boolean test(UserProfileData profileData);
}
