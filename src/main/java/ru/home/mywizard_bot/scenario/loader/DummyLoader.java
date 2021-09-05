package ru.home.mywizard_bot.scenario.loader;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.Illustration;
import ru.home.mywizard_bot.scenario.Item;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.actions.Action;
import ru.home.mywizard_bot.scenario.actions.Event;
import ru.home.mywizard_bot.scenario.actions.InlineLink;
import ru.home.mywizard_bot.scenario.actions.MovementLink;
import ru.home.mywizard_bot.scenario.checks.ActiveGameCheck;
import ru.home.mywizard_bot.scenario.checks.EnemyDead;
import ru.home.mywizard_bot.scenario.checks.EventCheck;
import ru.home.mywizard_bot.scenario.checks.PlayerDead;
import ru.home.mywizard_bot.scenario.features.*;
import ru.home.mywizard_bot.utils.Emojis;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyLoader extends Loader {
    @Override
    public void loadExtraLinks() {
        List<MovementLink> links;

        //Extra menu buttons for SHOW_MAIN_MENU
        links = new ArrayList<>();
        //links.add(new Link("Купи игру! Игру купи!", "buyTheGame"));
        extraLinks.put(BotState.SHOW_MAIN_MENU, links);

        //Extra menu buttons for PLAY_SCENARIO
        links = new ArrayList<>();
        links.add(new MovementLink("Листок путешественника", "inventory", new ShowInventory()));
        links.add(new MovementLink("Меню", "10000", new SetStateMenu()));
        //links.add(new Link("Меню", "10000", new SetStateMenu()));
        extraLinks.put(BotState.PLAY_SCENARIO, links);

        //Extra menu buttons for COMBAT
        links = new ArrayList<>();
        //links.add(new Link("Меню", "10000", new SetStateMenu()));
        extraLinks.put(BotState.COMBAT, links);
    }

    @Override
    public void loadParagraphs() {
        String id;
        Paragraph paragraph;
        List<Action> actions;

        id = "noMenuParagraph";
        paragraph = new Paragraph(id, "Раздел отсутствует :(");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Вернуться в главное меню", "10000"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "inventory";
        paragraph = new Paragraph(id, "");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Восстановить здоровье едой", "inventory"));
        actions.add(new InlineLink("Описание телепатических способностей", "inventory"));
        actions.add(new InlineLink("Вернуться в игру", "inventory", new ReturnToGame()));
        paragraph.setActions(actions);
        //paragraph.addFeature(new ShowInventory());
        allParagraphs.put(id, paragraph);

        id = "initialMenu";
        paragraph = new Paragraph(id, "Добро пожаловать в игру \"Повелитель безбрежной пустыни\" от PLAY_the_GAME\\!");
        paragraph.addText("Для ознакомления с правилами игры, рекомендуем ознакомиться с Руководством.");
        paragraph.setIllustration(new Illustration("", "static/images/art_1.jpg"));
        actions = new ArrayList<>();
        actions.add(new InlineLink("Руководство", "10003", false));
        actions.add(new InlineLink("Новая игра", "10002", false));
        actions.add(new InlineLink("Статистика", "10004", false));
        actions.add(new InlineLink("Подробнее о боте", "10005", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "10000";
        paragraph = new Paragraph(id, "Главное меню");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Вернуться в игру", "10001", new ActiveGameCheck(), new ReturnToGame(), true));
        actions.add(new InlineLink("Новая игра", "10002", false));
        actions.add(new InlineLink("Руководство", "10003", false));
        actions.add(new InlineLink("Статистика", "10004", false));
        actions.add(new InlineLink("Подробнее о боте", "10005", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "10002";
        paragraph = new Paragraph(id, "Вы уверены?");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Да", "0", true));
        actions.add(new InlineLink("Назад", "10000", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "10003";
        paragraph = new Paragraph(id, "Выберите интересующую тему");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Правила игры", "10008"));
        actions.add(new InlineLink("Ловкость и сила", "10009"));
        actions.add(new InlineLink("Удача", "10010"));
        actions.add(new InlineLink("Битвы", "10011"));
        actions.add(new InlineLink("Ранения", "10012"));
        actions.add(new InlineLink("Сила мысли", "10013"));
        actions.add(new InlineLink("Если вы проголодались", "10014"));
        actions.add(new InlineLink("Листок путешественника", "10015"));
        actions.add(new InlineLink("Назад", "10000", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "10004";
        paragraph = new Paragraph(id, "Выберите раздел");
        paragraph.addText("Если не боитесь, конечно...");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Персональная статистика", "10016"));
        actions.add(new InlineLink("Глобальная статистика", "10017"));
        actions.add(new InlineLink("Назад", "10000", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "10005";
        paragraph = new Paragraph(id, "Выберите раздел");
        actions = new ArrayList<>();
        actions.add(new InlineLink("О сценарии", "10018", false));
        actions.add(new InlineLink("О разработчиках", "10019"));
        actions.add(new InlineLink("Назад", "10000", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "10019";
        paragraph = new Paragraph(id, "Имена этих людей слишком хорошо известны, чтобы их называть\\!");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Понятно, очень приятно", "10005"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "10018";
        paragraph = new Paragraph(id, "Хороший сценарий?");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Да!", "10005"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "-1";
        paragraph = new Paragraph(id, "Внезапно из-за спазма в горле вы давитесь собственной слюной и умираете " +
                "так и не достигнув цели своего путешествия.");
        actions = new ArrayList<>();
        actions.add(new Event(new EndGame()));
        actions.add(new MovementLink("Начать заново", "10002"));
        actions.add(new MovementLink("Выйти в главное меню", "10000"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "combatDefeat";
        paragraph = new Paragraph(id, "Вам не хватило сил одержать победу в этой схватке. Вам конец " + Emojis.SKELETON);
        actions = new ArrayList<>();
        actions.add(new Event(new EndGame()));
        actions.add(new MovementLink("Начать заново", "10002"));
        actions.add(new MovementLink("Выйти в главное меню", "10000"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "0";
        paragraph = new Paragraph(id, "AN IMMORTALIS ES?");
        paragraph.setIllustration(new Illustration("", "static/images/barbarian_1.jpg"));
        actions = new ArrayList<>();
        actions.add(new MovementLink("EX ANIMO, FRATER\\!", "1", new SetStateScenario()));
        /*paragraph.addFeature(new InitNewGame());
        paragraph.addFeature(new GiveItem(new Item("Sword", "Меч-хладинец")));*/
        actions.add(new Event(new InitNewGame()));
        actions.add(new Event(new GiveItem(new Item("Sword", "Меч-хладинец"))));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "1";
        paragraph = new Paragraph(id, "Плодородные земли Элгариола позади. Перед вами пустыня. Вряд ли удастся " +
                "обнаружить здесь тропинку, а тем более какую-нибудь дорогу, так что придется ориентироваться  только " +
                "по солнцу. Одинокая гора должна быть где-то на востоке, но стоит ли отправляться туда сразу? "+//);
        //paragraph.addText(
                "В деревушке, раскинувшейся у самого края пустыни, вы узнали, что древний караванный путь уходит на " +
                "юго-восток. Там могут даже встретиться еще не пересохшие оазисы. Путь на северо-восток ведет к горам " +
                "Лонсам. Куда направитесь вы?");
        actions = new ArrayList<>();
        actions.add(new MovementLink("На северо-восток", "89", new EventCheck("MaylineIsDead")));
        actions.add(new MovementLink("На юго-восток", "230"));
        //actions.add(new InlineLink("Проверить удачу " + Emojis.DICE, "badLuck", new LuckCheck("goodLuck", "badLuck"), true));

        MovementLink link = new MovementLink("Вернуться и подраться с Майлином", "999", new EventCheck("MaylineIsDead", false));
        link.addCondition(new EventCheck("MaylineIsLaugh", false));
        actions.add(link);

        link = new MovementLink("Майлин смеялся при виде ваших пяток. Нужно ему отомстить!", "999", new EventCheck("MaylineIsLaugh"));
        link.addCondition(new EventCheck("MaylineIsDead", false));
        actions.add(link);
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "goodLuck";
        paragraph = new Paragraph(id, "Фортуна к Вам благосклонна. Вы внезапно получаете телеграмму, что повелитель мёртв и вы победили! Это успех!");
        actions = new ArrayList<>();
        actions.add(new MovementLink("Победа!", "10000", new SetStateMenu()));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "badLuck";
        paragraph = new Paragraph(id, "Вам и не повезло и повезло. Вы неудачливы, но ничего не происходит.");
        actions = new ArrayList<>();
        actions.add(new MovementLink("Продолжить", "1"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "999";
        paragraph = new Paragraph(id, "Ах, так\\!? - вскрикивает маг и хватается за посох и вручает вам веревку\\. Не ждите пощады\\!");
        paragraph.setIllustration(new Illustration("", "static/images/monster_2.jpg"));
        actions = new ArrayList<>();
        actions.add(new Event(new SetStateCombat()));
        actions.add(new Event(new AddCombatCheck()));
        actions.add(new Event(new GiveItem(new Item("Rope", "Веревка"))));
        /*link = new MovementLink("Трусливо сбежать", "1", new EnemyAlive(), new GiveItem(new Item("MaylineIsLaugh", "MaylineIsLaugh", false)));
        link.addEffect(new SetStateScenario());
        actions.add(link);*/
        actions.add(new MovementLink("Продолжить", "999-1", new EnemyDead(), new GiveItem(new Item("MaylineIsDead", "MaylineIsDead", false))));
        actions.add(new MovementLink("Путешествие окончено" + Emojis.SCULL_BONES, "combatDefeat", new PlayerDead()));
        actions.add(new ru.home.mywizard_bot.scenario.actions.Enemy("Майлин", "Mayline", 10, 12, 25));
        actions.add(new ru.home.mywizard_bot.scenario.actions.Enemy("Король Элгариола", "King", 10, 12, 25));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "999-1";
        paragraph = new Paragraph(id, "С трудом, но вы одолеваете волшебника, но какой в этом был смысл?");
        actions = new ArrayList<>();
        actions.add(new MovementLink("Никто не смеет называть меня трусом!", "1"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "1000";
        paragraph = new Paragraph(id, "Победа! Теперь можно никуда не ходить.");
        actions = new ArrayList<>();
        actions.add(new MovementLink("Начать заново", "1", new GiveItem(new Item("MaylineIsDead", "MaylineIsDead", false))));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "-2";
        paragraph = new Paragraph(id, "Слабак! Тряпка! Дешёвка! Ты мёртв! - говорит Майлин скармливая ваш труп " +
                "кондорам. Однако во всём есть свои плюсы - теперь вам не нужно идти через пустыню :)");
        actions = new ArrayList<>();
        actions.add(new MovementLink("Начать заново", "1"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "89";
        paragraph = new Paragraph(id, "Решив, что хребет Лонсам все же предпочтительнее Мортлэндских болот, " +
                "направляетесь на северо-восток. К полудню солнце печет настолько сильно, что вы уже еле-еле " +
                "передвигаете ноги, а ведь это только первый день пути. К тому же глаза сильно устают от бескрайнего " +
                "моря песка, да и идти по пустыне гораздо труднее, чем по ровной и, главное, твердой земле. Вы " +
                "начинаете мечтать об отдыхе, но вокруг нет даже намека на оазис, а опускаться на раскаленный песок " +
                "под полуденным солнцем вряд ли имеет смысл. Поднимается ветер (хорошо бы еще не было песчаной бури). " +
                "Но вместе с ветром появляется и первая растительность — несколько шаров перекати-поле, быстро " +
                "катящихся навстречу. Что вы предпримите?");
        actions = new ArrayList<>();
        actions.add(new MovementLink("Свернуть в сторону", "514"));
        actions.add(new MovementLink("Продолжать путь", "479"));
        actions.add(new MovementLink("Остановиться и обножить меч", "344"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "230";
        paragraph = new Paragraph(id, "ВНЕЗАПНО вы находите монету с изображением профиля Императора\\! Это к удаче\\!");
        paragraph.addText("Путешествие по пустыне оказывается совсем не таким приятным, как это могло " +
                "показаться в королевском дворце в Элгариоле. С непривычки ноги вязнут в песке, пот заливает глаза, " +
                "пустыня давит и окружает со всех сторон. А ведь еще предстоит пробраться через Мортлэндские топи, а " +
                "еще... Лучше уж об этом не думать. Тем более что впереди что-то виднеется.");
        paragraph.addText("Через полчаса становится понятно, что это караван верблюдов, неспешно двигающийся с юга наперерез вам. Ваше действие?");
        //paragraph.addFeature(new GiveItem(new Item("Coin", "Монета с профилем Императора")));
        actions = new ArrayList<>();
        actions.add(new MovementLink("Свернуть на север, чтобы избежать встречи с ним", "481"));
        actions.add(new MovementLink("Идти дальше", "96"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "96";
        paragraph = new Paragraph(id, "Часа через полтора вы догоняете караван. Погонщик первого верблюда " +
                "выкрикивает какое-то протяжное слово на неизвестном языке, и все животные останавливаются. Смуглые " +
                "худощавые люди спрыгивают на землю. Одни начинают расседлывать усталых животных, другие разбивают " +
                "шатры. На вас никто не обращает внимания. Смеркается, и в самом деле пора подумать о ночлеге. " +
                "Хотите подойти поговорить с одним из погонщиков или продолжите свой путь, решив, что ночь безопасней " +
                "провести в одиночку, чем с незнакомыми людьми?");
        paragraph.setIllustration(new Illustration("Гном-погонщик пристально смотрит на вас.", "static/images/dwarf.jpg"));
        actions = new ArrayList<>();
        actions.add(new MovementLink("Поговорить с погонщиком", "345"));
        actions.add(new MovementLink("Продолжить путь", "245"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "345";
        paragraph = new Paragraph(id, "- Ты чьих будешь, - спрашивает погонщик.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Эм... ммм... Хоббитс, эээ?", "346", true));
        actions.add(new InlineLink("Я - головка от буя!", "347", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "346";
        paragraph = new Paragraph(id, "Ненавижу хоббитов! - кричит погонщик и хватается за саблю. Битва неизбежна.",
                "С трудом, но вы одолеваете пустынного жителя. Но что вы будете делать с остальными кочевниками?");
        actions = new ArrayList<>();
        actions.add(new Event(new SetStateCombat()));
        actions.add(new Event(new AddCombatCheck()));
        actions.add(new MovementLink("Продолжить", "347", new EnemyDead(), new GiveItem(new Item("MaylineIsDead", "MaylineIsDead", false))));
        actions.add(new MovementLink("Путешествие окончено" + Emojis.SCULL_BONES, "combatDefeat", new PlayerDead()));
        actions.add(new ru.home.mywizard_bot.scenario.actions.Enemy("Погонщик", "Drover", 10, 12, 25));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "245";
        paragraph = new Paragraph(id, "Пройдя мимо каравана, вы уходите в ночь, а усталые люди и верблюды вскоре " +
                "скрываются из виду. Пора и вам отдохнуть. Расположившись на ночлег, быстро засыпаете, ведь завтра " +
                "предстоит нелегкий путь");
        actions = new ArrayList<>();
        actions.add(new MovementLink("Отдыхать", "20"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "20";
        paragraph = new Paragraph(id, "Ночь приносит желанный отдых (можете восстановить 2 СИЛЫ), а утром решайте, " +
                "куда идти дальше.");
        actions = new ArrayList<>();
        actions.add(new MovementLink("На северо-восток", "116"));
        actions.add(new MovementLink("На юго-восток", "93"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);
    }
}
