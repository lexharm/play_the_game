package ru.home.mywizard_bot.scenario;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.features.Feature;

import java.util.ArrayList;
import java.util.List;

@Data
public class Paragraph implements Cloneable {
    String id;
    String text;
    List<String> textsList = new ArrayList<>();
    String postText;
    private List<Link> inlineLinks = new ArrayList<>();
    private List<Link> links = new ArrayList<>();
    private String imagePath;
    private boolean combat = false;
    private Enemy enemy;
    private List<Feature> features = new ArrayList<>();

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

    public Paragraph(String id, String text, boolean isCombat, Enemy enemy) {
        this.id = id;
        this.text = text;
        this.combat = isCombat;
        this.enemy = enemy;
    }

    public void engageFeatures(UserProfileData profileData) {
        for (Feature feature : features) {
            feature.engage(profileData);
        }
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
}
