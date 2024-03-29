package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Dice;
import ru.home.mywizard_bot.scenario.Story;
import ru.home.mywizard_bot.service.ApplicationContextHolder;
import ru.home.mywizard_bot.utils.Emojis;

public class LuckCheck implements Feature {
    private String goodLuckParagraphId;
    private String badLuckParagraphId;

    public LuckCheck(String goodLuckParagraphId, String badLuckParagraphId) {
        this.goodLuckParagraphId = goodLuckParagraphId;
        this.badLuckParagraphId = badLuckParagraphId;
    }

    @Override
    public void engage(UserProfileData profileData) {
        String resultId;
        String status;
        Story story = (Story) ApplicationContextHolder.getApplicationContext().getBean("story");
        int dice = Dice.roll();
        if (dice % 2 == 0) {
            resultId = goodLuckParagraphId;
            status = "Вы прошли проверку удачи " + Emojis.THUMBSUP;
        } else {
            resultId = badLuckParagraphId;
            status = "Вы не прошли проверку удачи " + Emojis.THUMBSUP;
        }
        status += "(" + dice + ")";
        profileData.setAdditionalStatus(status);
        profileData.setNewParagraph(story.getParagraph(resultId, BotState.PLAY_SCENARIO));
    }
}
