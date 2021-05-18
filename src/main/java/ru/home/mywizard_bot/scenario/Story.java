package ru.home.mywizard_bot.scenario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.scenario.loader.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Story {
    private Map<Integer, Paragraph> allParagraphs;

    public Story(Loader loader) {
        allParagraphs = loader.getAllParagraphs();
    }

    public Paragraph getParagraph(int id) {
        Paragraph paragraph;
        if (allParagraphs.containsKey(id)) {
            paragraph = allParagraphs.get(id);
        } else {
            paragraph = allParagraphs.get(-1);
        }
        return paragraph;
    }

    public Paragraph getParagraph(Link link) {
        Paragraph paragraph;
        if (allParagraphs.containsKey(link.id)) {
            paragraph = allParagraphs.get(link.id);
        } else {
            paragraph = allParagraphs.get(-1);
        }
        return paragraph;
    }
}
