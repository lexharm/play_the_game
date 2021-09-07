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
import ru.home.mywizard_bot.scenario.checks.InventoryCheck;
import ru.home.mywizard_bot.scenario.features.*;
import ru.home.mywizard_bot.utils.Condition;
import ru.home.mywizard_bot.utils.Emojis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PreAlphaLoader extends Loader {
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
        links.add(new MovementLink("Меню", "mainMenu", new SetStateMenu()));
        //links.add(new Link("Меню", "mainMenu", new SetStateMenu()));
        extraLinks.put(BotState.PLAY_SCENARIO, links);

        //Extra menu buttons for COMBAT
        links = new ArrayList<>();
        //links.add(new Link("Меню", "mainMenu", new SetStateMenu()));
        extraLinks.put(BotState.COMBAT, links);
    }

    @Override
    public void loadParagraphs() {
        String id;
        Paragraph paragraph;
        List<Action> actions;
        Map<String, Item> items = new HashMap<>();
        items.put("Sword", new Item("Sword", "Меч"));
        items.put("Food", new Item("Food", "Еда"));
        items.put("Gold", new Item("Gold", "Золото"));
        items.put("CaravanGold", new Item("CaravanGold", "CaravanGold", false));

        id = "noMenuParagraph";
        paragraph = new Paragraph(id, "Раздел отсутствует :(");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Вернуться в главное меню", "mainMenu"));
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
        paragraph = new Paragraph(id, "Добро пожаловать в игру \"Повелитель безбрежной пустыни\" от PLAY_the_GAME!\n" +
                "Используемые в игре материалы принадлежат их авторам.\n" +
                "Версия 0.1 (pre-alpha)");
        paragraph.setIllustration(new Illustration("", "static/images/art_1.jpg"));
        actions = new ArrayList<>();
        //actions.add(new InlineLink("Руководство", "10003", false));
        actions.add(new InlineLink("Новая игра", "newGameConfirm", false));
        //actions.add(new InlineLink("Статистика", "10004", false));
        //actions.add(new InlineLink("Подробнее о боте", "10005", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "mainMenu";
        paragraph = new Paragraph(id, "Главное меню");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Вернуться в игру", "resumeGame", new ActiveGameCheck(), new ReturnToGame(), true));
        actions.add(new InlineLink("Новая игра", "newGameConfirm", false));
        //actions.add(new InlineLink("Руководство", "10003", false));
        //actions.add(new InlineLink("Статистика", "10004", false));
        //actions.add(new InlineLink("Подробнее о боте", "10005", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "newGameConfirm";
        paragraph = new Paragraph(id, "Вы уверены?");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Да", "intro_8", true));
        actions.add(new InlineLink("Назад", "mainMenu", false));
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
        actions.add(new InlineLink("Назад", "mainMenu", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "10004";
        paragraph = new Paragraph(id, "Выберите раздел");
        paragraph.addText("Если не боитесь, конечно...");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Персональная статистика", "10016"));
        actions.add(new InlineLink("Глобальная статистика", "10017"));
        actions.add(new InlineLink("Назад", "mainMenu", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "10005";
        paragraph = new Paragraph(id, "Выберите раздел");
        actions = new ArrayList<>();
        actions.add(new InlineLink("О сценарии", "10018", false));
        actions.add(new InlineLink("О разработчиках", "10019"));
        actions.add(new InlineLink("Назад", "mainMenu", false));
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
        actions.add(new MovementLink("Начать заново", "newGameConfirm"));
        actions.add(new MovementLink("Выйти в главное меню", "mainMenu"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "combatDefeat";
        paragraph = new Paragraph(id, "Вам не хватило сил одержать победу в этой схватке. Вам конец " + Emojis.SKELETON);
        actions = new ArrayList<>();
        actions.add(new Event(new EndGame()));
        actions.add(new MovementLink("Начать заново", "newGameConfirm"));
        actions.add(new MovementLink("Выйти в главное меню", "mainMenu"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "intro_1";
        paragraph = new Paragraph(id, "С недавних пор враг то и дело стал нарушать покой некогда безмятежного " +
                "королевства Элгариол, ставшего со временем лишь затерянным островком в бурном и неспокойном океане " +
                "Черной магии. Сначала только чудом удалось предотвратить вторжение с юга зеленых рыцарей, гоблинов " +
                "и орков, выполнявших волю могущественного мага Барлада Дэрта. И вот уже грозит новая опасность — " +
                "теперь с востока. Издалека, из-за горной цепи Лонсам и Мортлэндских топей, стали доходить до " +
                "Элгариола слухи о новом чародее, сильнее и коварнее прежнего.");
        paragraph.setIllustration(new Illustration("", "static/images/barbarian_1.jpg"));
        actions = new ArrayList<>();
        actions.add(new InlineLink("Далее...", "intro_2", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "intro_2";
        paragraph = new Paragraph(id, "Узнав об этом, придворный астролог и советник Короля магистр Белой магии " +
                "Майлин отправил на восток своих разведчиков — огромных кондоров. Они способны без устали пролететь за " +
                "день сотни миль и многое увидеть, оставаясь незамеченными. Вернувшись, птицы рассказали, что по ту " +
                "сторону гор лежит бескрайняя пустыня, над которой высится одинокая скала. На многие мили вокруг не " +
                "встретили они ни человека, ни зверя, но Зло исходит именно от этой скалы. На следующий день все " +
                "кондоры погибли, бросившись из поднебесья на землю со сложенными крыльями.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Далее...", "intro_3", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "intro_3";
        paragraph = new Paragraph(id, "Собрав воедино все подвластное ему волшебство, Майлин смог понять, что " +
                "случилось с несчастными птицами. На третий день полета, когда, по всем расчетам, они должны были " +
                "находиться над той самой скалой, кондоры получили огромной силы мысленный приказ: вернуться назад " +
                "и покончить с собой. Враг не учел только одного — разведчики достигли Элгариола быстрее, чем он рассчитывал.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Далее...", "intro_4", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "intro_4";
        paragraph = new Paragraph(id, "Всякий раз, когда захватчики посягали на земли Элгариола, кроме храбрых " +
                "воинов защитой ему была и Белая магия. Теперь же возникло опасение, что даже Майлин не сможет " +
                "противостоять новому Врагу. Только настоящий герой и только в одиночку мог бы проникнуть в логово " +
                "Врага незамеченным, и Король попросил своего советника найти ему такого человека.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Далее...", "intro_5", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "intro_5";
        paragraph = new Paragraph(id, "Если вы готовы сражаться на стороне Добра против Зла, если не привыкли " +
                "отступать на полпути и не испугаетесь встречи с Черной магией, то вы именно тот человек, который и " +
                "нужен Королю Элгариола.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Далее...", "intro_6", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "intro_6";
        paragraph = new Paragraph(id, "О силе и храбрости Врага почти ничего не известно. Чтобы добраться до " +
                "пустыни, придется либо переходить горы Лонсам, либо пробираться через Мортлэндские топи. Ни один " +
                "житель Элгариола не заходил так далеко, поэтому карту вам придется рисовать самим,— если удастся " +
                "вернуться назад живыми, она будет бесценна для жителей королевства.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Далее...", "intro_7", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "intro_7";
        paragraph = new Paragraph(id, "Все, чем может помочь Майлин,— это дать вам оружие, при помощи которого " +
                "можно будет сразиться с Врагом. Раз тот пользуется телепатией, то и вам придется тренировать свою " +
                "Силу мысли. Однако время торопит, и в совершенстве овладеть умением отдавать мысленные приказы вы, " +
                "конечно, не успеете. Когда придет время отправиться в путь, ваша Сила мысли будет равна лишь 3, но " +
                "каждая новая победа будет прибавлять уверенности в себе и умения, повышая ваши телепатические способности.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Далее...", "intro_8", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "intro_8";
        paragraph = new Paragraph(id, "Путь долог, и вы берете с собой лишь самое необходимое: меч, на три дня " +
                "еды в заплечном мешке да еще 15 золотых из оскудевшей в последнее время королевской казны.");
        paragraph.addText("Через два дня пути по Главному Восточному тракту вы подходите к границе Элгариола. " +
                "Здесь королевские владения заканчиваются, дальше вам придется полагаться только на себя. " +
                "Если вы готовы пройти нехожеными тропами и испытать на себе всю силу Врага, не потеряв надежды " +
                "выйти победителем, то переверните страницу, и да сопутствует вам удача!");
        actions = new ArrayList<>();
        actions.add(new MovementLink("Вперед!", "1", new SetStateScenario()));
        actions.add(new Event(new InitNewGame()));
        actions.add(new Event(new GiveItem(items.get("Sword"))));
        actions.add(new Event(new GiveItem(items.get("Food"), 3)));
        actions.add(new Event(new GiveItem(items.get("Gold"), 15)));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "1";
        paragraph = new Paragraph(id, "Плодородные земли Элгариола позади. Перед вами пустыня. Вряд ли удастся " +
                "обнаружить здесь тропинку, а тем более какую-нибудь дорогу, так что придется ориентироваться  только " +
                "по солнцу. Одинокая гора должна быть где-то на востоке, но стоит ли отправляться туда сразу? ");
        paragraph.addText(
                "В деревушке, раскинувшейся у самого края пустыни, вы узнали, что древний караванный путь уходит на " +
                "юго-восток. Там могут даже встретиться еще не пересохшие оазисы. Путь на северо-восток ведет к горам " +
                "Лонсам. Куда направитесь вы?");
        actions = new ArrayList<>();
        actions.add(new MovementLink("На северо-восток", "89"));
        actions.add(new MovementLink("На юго-восток", "230"));
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
        paragraph = new Paragraph(id, "Путешествие по пустыне оказывается совсем не таким приятным, как это могло " +
                "показаться в королевском дворце в Элгариоле. С непривычки ноги вязнут в песке, пот заливает глаза, " +
                "пустыня давит и окружает со всех сторон. А ведь еще предстоит пробраться через Мортлэндские топи, а " +
                "еще... Лучше уж об этом не думать. Тем более что впереди что-то виднеется.");
        paragraph.addText("Через полчаса становится понятно, что это караван верблюдов, неспешно двигающийся с юга " +
                "наперерез вам. Ваше действие?");
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
        actions = new ArrayList<>();
        actions.add(new MovementLink("Поговорить с погонщиком", "345"));
        actions.add(new MovementLink("Продолжить путь", "245"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "345";
        paragraph = new Paragraph(id, "Нельзя сказать, чтобы погонщик был особенно рад вашему появлению.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Проверить обаяние", "346", new LuckCheck("629", "245"), true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "629";
        paragraph = new Paragraph(id, "Иноземец вежливо приветствует вас, но вы не понимаете ни слова. В конце " +
                "концов выясняется, что он говорит на языке Элгариола (хотя и очень плохо), и вы кое-как все же можете " +
                "объясниться. О чем вы спросите?");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Попроситься на ночлег", "250", true));
        actions.add(new InlineLink("Торговать", "512", true));
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

        id = "250";
        paragraph = new Paragraph(id, "Торговец настолько любезен, что укладывает вас спать прямо в своем шатре, " +
                "не взяв за это ни гроша. Если вы приобрели товары на сумму больше 8 золотых, то <a l:href=\"#n_413\"><strong>413</strong></a>, если же меньше или вовсе ничего не покупали — <a l:href=\"#n_540\"><strong>540");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Отдыхать", "413", new InventoryCheck(items.get("CaravanGold"), 8, Condition.MORE_EQUAL), true));
        actions.add(new InlineLink("Отдыхать", "540", new InventoryCheck(items.get("CaravanGold"), 8, Condition.LESS), true));
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