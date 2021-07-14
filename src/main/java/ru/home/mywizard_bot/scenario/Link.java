package ru.home.mywizard_bot.scenario;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.checks.Check;
import ru.home.mywizard_bot.scenario.features.Feature;

import java.util.ArrayList;
import java.util.List;

@Data
public class Link {
    String text;
    String id;
    List<Check> checks = new ArrayList<>();
    List<Feature> features = new ArrayList<>();
    Item item;
    boolean newMessage = true;

    public Link(String id) {
        this.id = id;
    }

    public Link(String text, String id) {
        this(id);
        this.text = text;
    }

    public Link(String text, String id, boolean newMessage) {
        this(text, id);
        this.newMessage = newMessage;
    }

    public Link(String text, String id, Check check) {
        this(text, id);
        checks.add(check);
    }

    public Link(String text, String id, Check check, boolean newMessage) {
        this(text, id, check);
        this.newMessage = newMessage;
    }

    public Link(String text, String id, Feature feature) {
        this(text, id);
        features.add(feature);
    }

    public Link(String text, String id, Feature feature, boolean newMessage) {
        this(text, id, feature);
        this.newMessage = newMessage;
    }

    public Link(String text, String id, Check check, Feature feature) {
        this(text, id, check);
        addFeature(feature);
    }

    public Link(String text, String id, Check check, Feature feature, boolean newMessage) {
        this(text, id, check, feature);
        this.newMessage = newMessage;
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

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    public void engageFeatures(UserProfileData profileData) {
        for (Feature feature : features) {
            feature.engage(profileData);
        }
    }
}
