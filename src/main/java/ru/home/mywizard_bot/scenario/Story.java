package ru.home.mywizard_bot.scenario;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.loader.Loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Getter
public class Story {
    private final Map<Integer, Paragraph> allParagraphs;
    private final Map<BotState, List<Link>> extraLinks;

    public Story(Loader loader) {
        allParagraphs = loader.getAllParagraphs();
        extraLinks = loader.getExtraLinks();
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

    public Paragraph getParagraph(Link link) throws NoLinkException {
        Paragraph paragraph;
        if (allParagraphs.containsKey(link.id)) {
            paragraph = allParagraphs.get(link.id);
        } else {
            throw new NoLinkException();
        }
        return paragraph;
    }
}
