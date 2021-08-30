package ru.home.mywizard_bot.scenario.actions;

import lombok.Data;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.checks.Check;
import ru.home.mywizard_bot.scenario.features.Feature;

import java.util.ArrayList;
import java.util.List;

@Data
public class Action {
    protected String caption;
    protected String id;
    protected List<Check> conditions = new ArrayList<>();
    protected List<Feature> effects = new ArrayList<>();
    protected boolean newMessage = true;

    public Action() {
    }

    public Action(String id) {
        this.id = id;
    }

    public Action(String caption, String id) {
        this(id);
        this.caption = caption;
    }

    public Action(String caption, String id, Check check) {
        this(caption, id);
        this.addCondition(check);
    }

    public Action(String caption, String id, Feature feature) {
        this(caption, id);
        this.addEffect(feature);
    }

    public Action(String caption, String id, Check check, Feature feature) {
        this(caption, id, check);
        this.addEffect(feature);
    }

    public Action(Check check) {
        this("", "");
        this.addCondition(check);
    }

    public Action(Feature feature) {
        this("", "");
        this.addEffect(feature);
    }

    public void addCondition(Check check) {
        conditions.add(check);
    }

    public void addEffect(Feature feature) {
        effects.add(feature);
    }

    public void applyEffects(UserProfileData profileData) {
        effects.forEach(x -> x.engage(profileData));
    }

    public boolean test(UserProfileData profileData) {
        return conditions.isEmpty() || conditions.stream().allMatch(x -> x.test(profileData));
    }
}
