package ru.home.mywizard_bot.scenario;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.loader.Loader;

import java.util.List;
import java.util.Map;

@Getter
@Component
public class Story {
    private final Map<String, Paragraph> allParagraphs;
    private final Map<BotState, List<Link>> extraLinks;
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

    public Paragraph getMenuParagraph(Link link) {
        return getParagraph(link.id, BotState.SHOW_MAIN_MENU);
    }

    public Paragraph getStoryParagraph(Link link) {
        return getParagraph(link.id, BotState.PLAY_SCENARIO);
    }

    public Paragraph getCombatParagraph(Link link, Paragraph currentParagraph) {
        String id = link.id;
        Paragraph paragraph = null;
        for (Link extraLink : extraLinks.get(BotState.COMBAT)) {
            if (id.equals(extraLink.id)) {
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

    public List<Link> getExtraLinks(BotState botState) {
        return extraLinks.get(botState);
    }

    public Paragraph getNoLinkParagraph() {
        return allParagraphs.get(noLinkParagraph);
    }

    public Paragraph getInitialMenuParagraph() {
        return getMenuParagraph(new Link(initialMenuParagraph));
    }

    public Paragraph getInitialStoryParagraph() {
        return getStoryParagraph(new Link(initialStoryParagraph));
    }

    public Paragraph getCombatDefeatParagraph() {
        return getStoryParagraph(new Link(combatDefeatParagraph));
    }
}
