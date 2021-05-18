package ru.home.mywizard_bot.scenario.loader;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.scenario.Paragraph;

import java.util.HashMap;
import java.util.Map;

@Component
public abstract class Loader {
    Map<Integer, Paragraph> allParagraphs = new HashMap<>();

    public Map<Integer, Paragraph> getAllParagraphs() {
        load();
        return allParagraphs;
    }

    public abstract void load();
}
