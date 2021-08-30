package ru.home.mywizard_bot.scenario.actions;

import ru.home.mywizard_bot.scenario.checks.Check;
import ru.home.mywizard_bot.scenario.features.Feature;

public class MovementLink extends Action {

    public MovementLink() {
    }

    public MovementLink(String caption, String id) {
        super(caption, id);
        this.newMessage = true;
    }

    public MovementLink(String caption, String id, boolean newMessage) {
        super(caption, id);
        this.newMessage = newMessage;
    }

    public MovementLink(String caption, String id, Check check) {
        super(caption, id, check);
    }

    public MovementLink(String caption, String id, Feature feature) {
        super(caption, id, feature);
    }

    public MovementLink(String caption, String id, Check check, Feature feature) {
        super(caption, id, check, feature);
    }
}
