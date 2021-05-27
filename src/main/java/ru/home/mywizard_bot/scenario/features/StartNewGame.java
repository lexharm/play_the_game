package ru.home.mywizard_bot.scenario.features;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.handlers.fillingprofile.UserProfileData;

public class StartNewGame implements Feature {
    @Value("${story.firstParagraph}")
    private Integer firstParagraph;
    @Override
    public void engage(UserProfileData profileData) {
        //profileData.setCurrentParagraph(firstParagraph);
    }
}
