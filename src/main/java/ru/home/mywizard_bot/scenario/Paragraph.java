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
    private List<Link> links = new ArrayList<>();
    private String imagePath;
    private boolean combat = false;
    private Enemy enemy;
    private List<Feature> features = new ArrayList<>();

    public Paragraph(String id, String text) {
        this.id = id;
        this.text = text;
        combat = false;
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
}
