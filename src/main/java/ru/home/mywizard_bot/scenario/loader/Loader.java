package ru.home.mywizard_bot.scenario.loader;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.Paragraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public abstract class Loader {
    Map<Integer, Paragraph> allParagraphs = new HashMap<>();
    Map<BotState, List<Link>> extraLinks = new HashMap<>();

    public Map<Integer, Paragraph> getAllParagraphs() {
        loadParagraphs();
        return allParagraphs;
    }

    public Map<BotState, List<Link>> getExtraLinks() {
        loadExtraLinks();
        return extraLinks;
    }

    public abstract void loadParagraphs();

    public abstract void loadExtraLinks();
}
