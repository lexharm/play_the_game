package ru.home.mywizard_bot.scenario.loader;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.actions.MovementLink;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpecificLoader extends Loader {
    @Override
    public void loadParagraphs() {
        // Vova, implement your code here...
    }

    @Override
    public void loadExtraLinks() {
        List<MovementLink> links;

        //Extra menu buttons for SHOW_MAIN_MENU
        links = new ArrayList<>();
        extraLinks.put(BotState.SHOW_MAIN_MENU, links);

        //Extra menu buttons for PLAY_SCENARIO
        links = new ArrayList<>();
        /*links.add(new Link("Листок путешественника", "9000"));
        links.add(new Link("Меню", "10000", new SetStateMenu()));*/
        extraLinks.put(BotState.PLAY_SCENARIO, links);

        //Extra menu buttons for COMBAT
        links = new ArrayList<>();
        //links.add(new Link("Меню", "10000", new SetStateMenu()));
        extraLinks.put(BotState.COMBAT, links);
    }
}
