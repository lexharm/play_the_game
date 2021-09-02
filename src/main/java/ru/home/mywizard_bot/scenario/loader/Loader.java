package ru.home.mywizard_bot.scenario.loader;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.actions.MovementLink;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public abstract class Loader {
    Map<String, Paragraph> allParagraphs = new HashMap<>();
    Map<BotState, List<MovementLink>> extraLinks = new HashMap<>();

    public Map<String, Paragraph> getAllParagraphs() {
        loadParagraphs();
        return allParagraphs;
    }

    public Map<BotState, List<MovementLink>> getExtraLinks() {
        loadExtraLinks();
        return extraLinks;
    }

    public abstract void loadParagraphs();

    public abstract void loadExtraLinks();
}
