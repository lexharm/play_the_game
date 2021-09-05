package ru.home.mywizard_bot.scenario;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.actions.Action;
import ru.home.mywizard_bot.scenario.actions.MovementLink;
import ru.home.mywizard_bot.scenario.loader.Loader;

import java.util.List;
import java.util.Map;

@Getter
@Component
public class Story {
    private final Map<String, Paragraph> allParagraphs;
    private final Map<BotState, List<MovementLink>> extraLinks;
    @Value("${story.noLinkParagraph}")
    private String noLinkParagraph;
    @Value("${story.noStoryParagraph}")
    private String noStoryParagraph;
    @Value("${story.noMenuParagraph}")
    private String noMenuParagraph;
    @Value("${story.initialMenuParagraph}")
    private String initialMenuParagraph;
    @Value("${story.initialStoryParagraph}")
    private String initialStoryParagraph;
    @Value("${story.combatDefeatParagraph}")
    private String combatDefeatParagraph;

    @Value("${startNewGame.strength}")
    private int strength;
    @Value("${startNewGame.dexterity}")
    private int dexterity;
    @Value("${startNewGame.thoughtPower}")
    private int thoughtPower;
    @Value("${startNewGame.damage}")
    private int damage;

    public Story(@Qualifier("WorkingLoader") Loader loader) {
        allParagraphs = loader.getAllParagraphs();
        extraLinks = loader.getExtraLinks();
    }

    public Paragraph getParagraph(String id, BotState botState) {
        Paragraph paragraph;
        if (allParagraphs.containsKey(id)) {
            paragraph = allParagraphs.get(id);
        } else {
            if (botState == BotState.SHOW_MAIN_MENU)
                paragraph = allParagraphs.get(noMenuParagraph);
            else if (botState == BotState.PLAY_SCENARIO)
                paragraph = allParagraphs.get(noStoryParagraph);
            else
                paragraph = allParagraphs.get(noLinkParagraph);
        }
        Paragraph res = null;
        try {
            res = (Paragraph) paragraph.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return res;
    }

    public Paragraph getMenuParagraph(Action link) {
        return getParagraph(link.getId(), BotState.SHOW_MAIN_MENU);
    }

    public Paragraph getStoryParagraph(Action link) {
        return getParagraph(link.getId(), BotState.PLAY_SCENARIO);
    }

    public Paragraph getCombatParagraph(Action link, Paragraph currentParagraph) {
        String id = link.getId();
        Paragraph paragraph = null;
        for (MovementLink extraLink : extraLinks.get(BotState.COMBAT)) {
            if (id.equals(extraLink.getId())) {
                paragraph = allParagraphs.getOrDefault(id, currentParagraph);
            }
        }
        Paragraph res = null;
        if (paragraph == null) {
            res = getStoryParagraph(link);
        } else {
            try {
                res = (Paragraph) paragraph.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public List<MovementLink> getExtraLinks(BotState botState) {
        return extraLinks.get(botState);
    }

    public Paragraph getNoLinkParagraph() {
        return allParagraphs.get(noLinkParagraph);
    }

    public Paragraph getInitialMenuParagraph() {
        return getMenuParagraph(new Action(initialMenuParagraph));
    }

    public Paragraph getInitialStoryParagraph() {
        return getStoryParagraph(new Action(initialStoryParagraph));
    }

    public Paragraph getCombatDefeatParagraph() {
        return getStoryParagraph(new Action(combatDefeatParagraph));
    }
}
