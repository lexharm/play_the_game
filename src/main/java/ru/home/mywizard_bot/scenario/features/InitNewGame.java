package ru.home.mywizard_bot.scenario.features;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.ApplicationContextHolder;

import java.util.ArrayList;
import java.util.HashMap;

@Component
public class InitNewGame implements Feature {

    @Override
    public void engage(UserProfileData profileData) {
        Story story = (Story) ApplicationContextHolder.getApplicationContext().getBean("story");
        profileData.setCurrentParagraph(story.getInitialStoryParagraph());
        profileData.setStrength(story.getStrength());
        profileData.setInitStrength(story.getStrength());
        profileData.setDexterity(story.getDexterity());
        profileData.setInitDexterity(story.getDexterity());
        profileData.setThoughtPower(story.getThoughtPower());
        profileData.setInitThoughtPower(story.getThoughtPower());
        profileData.setDamage(story.getDamage());
        profileData.setInventory(new HashMap<>());
        profileData.setChecks(new HashMap<>());
        profileData.setEnemies(new ArrayList<>());
        profileData.setActiveGame(true);
    }
}
