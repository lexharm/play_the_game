package ru.home.mywizard_bot.scenario.checks;

import ru.home.mywizard_bot.model.UserProfileData;

public abstract class Check {

    public Check() {}

    public abstract boolean test(UserProfileData profileData);
}
