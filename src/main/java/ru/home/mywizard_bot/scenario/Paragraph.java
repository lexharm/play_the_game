package ru.home.mywizard_bot.scenario;

import lombok.Data;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.actions.Action;
import ru.home.mywizard_bot.scenario.actions.Enemy;
import ru.home.mywizard_bot.scenario.actions.Event;
import ru.home.mywizard_bot.scenario.actions.InlineLink;
import ru.home.mywizard_bot.scenario.actions.MovementLink;
import ru.home.mywizard_bot.scenario.features.Feature;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class Paragraph implements Cloneable, Serializable {
    String id;
    String text;
    List<String> textsList = new ArrayList<>();
    String postText;
    private List<Link> inlineLinks = new ArrayList<>();
    private List<Link> links = new ArrayList<>();
    private String imagePath;
    private boolean combat = false;
    private List<Feature> features = new ArrayList<>();
    private List<Action> actions = new ArrayList<>();
    private Illustration illustration;

    public Paragraph() {}

    public Paragraph(String id, String text) {
        this.id = id;
        //this.text = text;
        textsList.add(text);
        combat = false;
        postText = "";
    }

    public Paragraph(String id, String text, String postText) {
        this(id, text);
        this.postText = postText;
    }

    public void engageFeatures(UserProfileData profileData) {
        for (Feature feature : features) {
            feature.engage(profileData);
        }
    }

    public void applyActions(UserProfileData profileData) {
        actions.stream().filter(Event.class::isInstance)
                .filter(x -> x.test(profileData))
                .forEach(x -> x.applyEffects(profileData));
    }

    public void addFeature(Feature feature) {
        features.add(feature);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void addText(String text) {
        textsList.add(text);
    }

    public List<MovementLink> getMovementLinks() {
        //return actions.stream().filter(MovementLink.class::isInstance).collect(Collectors.toList());
        return actions.stream().filter(MovementLink.class::isInstance).map(MovementLink.class::cast).collect(Collectors.toList());
    }

    public List<Action> getInlineLinks1() {
        //return actions.stream().filter(InlineLink.class::isInstance).collect(Collectors.toList());
        return actions.stream().filter(InlineLink.class::isInstance).map(InlineLink.class::cast).collect(Collectors.toList());
    }

    public List<Enemy> getEnemies() {
        return actions.stream().filter(Enemy.class::isInstance).map(Enemy.class::cast).collect(Collectors.toList());
    }
}
