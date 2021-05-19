package ru.home.mywizard_bot.scenario;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.checks.Check;

import java.util.ArrayList;
import java.util.List;

@Data
public class Link {
    String text;
    int id;
    List<Check> checks = new ArrayList<>();
    Item item;

    public Link(String text, int id) {
        this.text = text;
        this.id = id;
    }

    public Link(String text, int id, Check check) {
        this.text = text;
        this.id = id;
        checks.add(check);
    }

    public Link(String text, int id, Item item) {
        this.text = text;
        this.id = id;
        this.item = item;
    }

    public boolean test(UserProfileData profileData) {
        for (Check check : checks) {
            if (!check.test(profileData)) {
                return false;
            }
        }
        return true;
    }

    public void addCheck(Check check) {
        checks.add(check);
    }

    public void reward(UserProfileData profileData) {
        if (item != null) {
            profileData.addItem(item);
        }
    }
}
