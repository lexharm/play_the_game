package ru.home.mywizard_bot.scenario.features;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.ApplicationContextHolder;

import java.util.HashMap;

@Component
public class StartNewGame implements Feature {

    @Override
    public void engage(UserProfileData profileData) {
        Story story = (Story) ApplicationContextHolder.getApplicationContext().getBean("story");
        profileData.setCurrentParagraph(story.getInitialStoryParagraph());
        profileData.setStrength(story.getStrength());
        profileData.setDexterity(story.getDexterity());
        profileData.setDamage(story.getDamage());
        profileData.setInventory(new HashMap<>());
        profileData.setChecks(new HashMap<>());
        profileData.clearEnemy();
        profileData.setBotState(BotState.PLAY_SCENARIO);
    }
}
