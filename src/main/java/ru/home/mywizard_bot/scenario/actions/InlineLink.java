package ru.home.mywizard_bot.scenario.actions;

import ru.home.mywizard_bot.scenario.checks.Check;
import ru.home.mywizard_bot.scenario.features.Feature;

public class InlineLink extends Action {

    public InlineLink() {
    }

    public InlineLink(String caption, String id) {
        super(caption, id);
        this.newMessage = false;
    }

    public InlineLink(String caption, String id, boolean newMessage) {
        this(caption, id);
        this.newMessage = newMessage;
    }

    public InlineLink(String caption, String id, Check check) {
        super(caption, id, check);
    }

    public InlineLink(String caption, String id, Feature feature) {
        super(caption, id, feature);
        //this.newMessage = false;
    }

    public InlineLink(String caption, String id, Check check, Feature feature) {
        super(caption, id, check, feature);
    }
}
