package ru.home.mywizard_bot.scenario.features;

import ru.home.mywizard_bot.model.UserProfileData;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.actions.Action;
import ru.home.mywizard_bot.scenario.actions.InlineLink;

import java.util.ArrayList;
import java.util.List;

public class ShowInventory implements Feature {
    @Override
    public void engage(UserProfileData profileData) {
        Paragraph paragraph;
        List<Action> actions;

        paragraph = new Paragraph("inventory", profileData.showInventory());
        actions = new ArrayList<>();
        actions.add(new InlineLink("Вернуться в игру", profileData.getCurrentParagraph().getId(), true));
        paragraph.setActions(actions);
        profileData.setNewParagraph(paragraph);
    }
}
