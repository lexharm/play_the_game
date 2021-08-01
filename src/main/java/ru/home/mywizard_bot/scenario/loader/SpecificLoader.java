package ru.home.mywizard_bot.scenario.loader;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.*;
import ru.home.mywizard_bot.scenario.actions.Action;
import ru.home.mywizard_bot.scenario.actions.Enemy;
import ru.home.mywizard_bot.scenario.actions.Event;
import ru.home.mywizard_bot.scenario.actions.InlineLink;
import ru.home.mywizard_bot.scenario.actions.MovementLink;
import ru.home.mywizard_bot.scenario.checks.*;
import ru.home.mywizard_bot.scenario.features.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpecificLoader extends Loader {
    @Override
    public void loadParagraphs() {
        String id;
        Paragraph paragraph;
        List<Action> actions;

        id = "noMenuParagraph";
        paragraph = new Paragraph(id, "Раздел отсутствует :(");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Вернуться в главное меню", "initialMenu"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "-1";
        paragraph = new Paragraph(id, "Внезапно из-за спазма в горле вы давитесь собственной слюной и умираете " +
                "так и не достигнув цели своего путешествия.");
        actions = new ArrayList<>();
        actions.add(new Event(new EndGame()));
        actions.add(new MovementLink("Начать заново", "0"));
        actions.add(new MovementLink("Выйти в главное меню", "initialMenu"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "initialMenu";
        paragraph = new Paragraph(id, "Добро пожаловать в игру \"Повелитель безбрежной пустыни\" от PLAY_the_GAME\\!");
        paragraph.addText("Для ознакомления с правилами игры, рекомендуем ознакомиться с Руководством.");
        paragraph.setIllustration(new Illustration("", "static/images/art_1.jpg"));
        actions = new ArrayList<>();
        actions.add(new InlineLink("Новая игра", "0", false));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "0";
        paragraph = new Paragraph(id, "С недавних пор враг то и дело стал нарушать покой некогда безмятежного королевства Элгариол, ставшего со" +
                "временем лишь затерянным островком в бурном и неспокойном океане Черной магии. Сначала только чудом удалось " +
                "предотвратить вторжение с юга зеленых рыцарей, гоблинов и орков, выполнявших волю могущественного мага Барлада " +
                "Дэрта. И вот уже грозит новая опасность — теперь с востока. Издалека, из-за горной цепи Лонсам и Мортлэндских " +
                "топей, стали доходить до Элгариола слухи о новом чародее, сильнее и коварнее прежнего. " +
                "Узнав об этом, придворный астролог и советник Короля магистр Белой магии Майлин отправил на восток своих " +
                "разведчиков — огромных кондоров. Они способны без устали пролететь за день сотни миль и многое увидеть, " +
                "оставаясь незамеченными. Вернувшись, птицы рассказали, что по ту сторону гор лежит бескрайняя пустыня, над " +
                "которой высится одинокая скала. На многие мили вокруг не встретили они ни человека, ни зверя, но Зло исходит " +
                "именно от этой скалы. На следующий день все кондоры погибли, бросившись из поднебесья на землю со сложенными " +
                "крыльями.");
        paragraph.addText("Собрав воедино все подвластное ему волшебство, Майлин смог понять, что случилось с несчастными птицами. На " +
                "третий день полета, когда, по всем расчетам, они должны были находиться над той самой скалой, кондоры получили " +
                "огромной силы мысленный приказ: вернуться назад и покончить с собой. Враг не учел только одного — разведчики " +
                "достигли Элгариола быстрее, чем он рассчитывал." +
                "Всякий раз, когда захватчики посягали на земли Элгариола, кроме храбрых воинов защитой ему была и Белая магия. " +
                "Теперь же возникло опасение, что даже Майлин не сможет противостоять новому Врагу. Только настоящий герой и " +
                "только в одиночку мог бы проникнуть в логово Врага незамеченным, и Король попросил своего советника найти ему " +
                "такого человека.");
        paragraph.addText("Если вы готовы сражаться на стороне Добра против Зла, если не привыкли отступать на полпути и не испугаетесь " +
                "встречи с Черной магией, то вы именно тот человек, который и нужен Королю Элгариола. " +
                "О силе и храбрости Врага почти ничего не известно. Чтобы добраться до пустыни, придется либо переходить горы " +
                "Лонсам, либо пробираться через Мортлэндские топи. Ни один житель Элгариола не заходил так далеко, поэтому карту " +
                "вам придется рисовать самим,— если удастся вернуться назад живыми, она будет бесценна для жителей королевства. " +
                "Все, чем может помочь Майлин,— это дать вам оружие, при помощи которого можно будет сразиться с Врагом. Раз тот " +
                "пользуется телепатией, то и вам придется тренировать свою Силу мысли. Однако время торопит, и в совершенстве " +
                "овладеть умением отдавать мысленные приказы вы, конечно, не успеете. Когда придет время отправиться в путь, ваша " +
                "Сила мысли будет равна лишь 3, но каждая новая победа будет прибавлять уверенности в себе и умения, повышая ваши " +
                "телепатические способности.");
        paragraph.addText("Путь долог, и вы берете с собой лишь самое необходимое: меч, на три дня еды в заплечном мешке да еще 15 золотых " +
                "из оскудевшей в последнее время королевской казны. " +
                "Через два дня пути по Главному Восточному тракту вы подходите к границе Элгариола. Здесь королевские владения " +
                "заканчиваются, дальше вам придется полагаться только на себя. Если вы готовы пройти нехожеными тропами и " +
                "испытать на себе всю силу Врага, не потеряв надежды выйти победителем, то переверните страницу, и да сопутствует " +
                "вам удача!");
        paragraph.setIllustration(new Illustration("", "static/images/barbarian_1.jpg"));
        actions = new ArrayList<>();
        actions.add(new MovementLink("Вперед в бой!", "1", new SetStateScenario()));
        actions.add(new Event(new InitNewGame()));  // Пометка параграфа в сценарии что он первый в игре и для него вызвать new Event(new InitNewGame())
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "1";
        paragraph = new Paragraph(id, "Плодородные земли Элгариола позади. Перед вами пустыня. Вряд ли удастся " +
                "обнаружить здесь тропинку, а тем более какую-нибудь дорогу, так что придется ориентироваться  только " +
                "по солнцу. Одинокая гора должна быть где-то на востоке, но стоит ли отправляться туда сразу?");
        paragraph.addText("В деревушке, раскинувшейся у самого края пустыни, вы узнали, что древний караванный путь уходит на " +
                "юго-восток. Там могут даже встретиться еще не пересохшие оазисы. Путь на северо-восток ведет к горам " +
                "Лонсам. Куда направитесь вы?");
        actions = new ArrayList<>();
        InlineLink inlineLink = new InlineLink("Купить изумрудный браслет за 1-у силу мысли", "89");
        inlineLink.addCondition(new ThoughtPowerEquals(3));
        inlineLink.addEffect(new GiveItem(new Item("emeraldsBracelet","Изумрудный браслет")));
        inlineLink.addEffect(new IncThoughtPower(-1));
        actions.add(inlineLink);
        actions.add(new MovementLink("На северо-восток", "89"));
        actions.add(new MovementLink("На юго-восток", "230"));

        MovementLink link = new MovementLink("Вернуться и подраться с Майлином", "999", new EventCheck("MaylineIsDead", false));
        link.addCondition(new EventCheck("MaylineIsLaugh", false));
        actions.add(link);

        link = new MovementLink("Майлин смеялся при виде ваших пяток. Нужно ему отомстить\\!", "999", new EventCheck("MaylineIsLaugh"));
        link.addCondition(new EventCheck("MaylineIsDead", false));
        actions.add(link);
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);


        id = "89";
        paragraph = new Paragraph(id, "Решив, что хребет Лонсам все же предпочтительнее Мортлэндских болот, направляетесь на северо-восток. К полудню " +
                "солнце печет настолько сильно, что вы уже еле-еле передвигаете ноги, а ведь это только первый день пути. К тому же " +
                "глаза сильно устают от бескрайнего моря песка, да и идти по пустыне гораздо труднее, чем по ровной и, главное, " +
                "твердой земле. Вы начинаете мечтать об отдыхе, но вокруг нет даже намека на оазис, а опускаться на раскаленный " +
                "песок под полуденным солнцем вряд ли имеет смысл. Поднимается ветер (хорошо бы еще не было песчаной бури). Но " +
                "вместе с ветром появляется и первая растительность — несколько шаров перекати-поле, быстро катящихся навстречу. " +
                "Попытаетесь свернуть в сторону, будете продолжать путь как ни в чем не бывало или остановитесь и обнажите меч?");
        actions = new ArrayList<>();
        actions.add(new Enemy("Невидимый страж", "InvisibleGuard", 1, 0, 2));
        actions.add(new Event(new SetStateCombat()));
        actions.add(new Event(new AddCombatCheck()));
        actions.add(new MovementLink("Продолжить", "afterInvisibleGuard", new EnemyDead()));
        actions.add(new Event(new GiveItem(new Item("emeraldsBracelet","Изумрудный браслет"))));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "afterInvisibleGuard";
        paragraph = new Paragraph(id, "Ура победили!");
        paragraph.addText("+1 сила мысли");
        actions = new ArrayList<>();
        actions.add(new Event(new IncThoughtPower(1)));
        actions.add(new InlineLink("Продолжить 4", "5", new ThoughtPowerEquals(4)));
        actions.add(new InlineLink("Продолжить 5", "6", new ThoughtPowerEquals(5)));
        actions.add(new InlineLink("Продолжить 6", "7", new ThoughtPowerEquals(6)));
        actions.add(new InlineLink("Продолжить 7", "8", new ThoughtPowerEquals(7)));
        actions.add(new InlineLink("Использовать браслет", "9", new InventoryCheck("emeraldsBracelet")));
        actions.add(new InlineLink("Нет браслета", "9", new InventoryCheck("emeraldsBracelet", false)));

        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "230";
        paragraph = new Paragraph(id, "Путешествие по пустыне оказывается совсем не таким приятным, как это могло показаться в королевском дворце в " +
                "Элгариоле. С непривычки ноги вязнут в песке, пот заливает глаза, пустыня давит и окружает со всех сторон. А ведь " +
                "еще предстоит пробраться через Мортлэндские топи, а еще... Лучше уж об этом не думать. Тем более что впереди " +
                "что-то виднеется. Через полчаса становится понятно, что это караван верблюдов, неспешно двигающийся с юга " +
                "наперерез вам. Свернете на север, чтобы избежать встречи с ним или пойдете дальше");
        actions = new ArrayList<>();
        actions.add(new MovementLink("избежать встречи", "481"));
        actions.add(new MovementLink("пойдете дальше", "96"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "999";
        paragraph = new Paragraph(id, "Ах, так\\!? - вскрикивает маг и хватается за посох и вручает вам веревку\\. Не ждите пощады\\!");
        paragraph.setIllustration(new Illustration("", "static/images/monster_2.jpg"));
        actions = new ArrayList<>();
        actions.add(new Event(new SetStateCombat()));
        actions.add(new Event(new AddCombatCheck()));
        actions.add(new Event(new GiveItem(new Item("Rope", "Веревка"))));
        link = new MovementLink("Трусливо сбежать", "1", new EnemyAlive(), new GiveItem(new Item("MaylineIsLaugh", "MaylineIsLaugh", false)));
        link.addEffect(new SetStateScenario());
        actions.add(link);
        actions.add(new MovementLink("Продолжить", "999-1", new EnemyDead(), new GiveItem(new Item("MaylineIsDead", "MaylineIsDead", false))));
        actions.add(new ru.home.mywizard_bot.scenario.actions.Enemy("Майлин", "Mayline", 10, 12, 25));
        //actions.add(new ru.home.mywizard_bot.scenario.actions.Enemy("Вова", "Vova", 10, 12, 25));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);
    }

    @Override
    public void loadExtraLinks() {
        List<Link> links;

        //Extra menu buttons for SHOW_MAIN_MENU
        links = new ArrayList<>();
        links.add(new Link("Купи игру\\! Игру купи\\!", "buyTheGame"));
        extraLinks.put(BotState.SHOW_MAIN_MENU, links);

        //Extra menu buttons for PLAY_SCENARIO
        links = new ArrayList<>();
        links.add(new Link("Листок путешественника", "inventory", new SetStateMenu()));
        links.add(new Link("Меню", "10000", new SetStateMenu()));
        extraLinks.put(BotState.PLAY_SCENARIO, links);

        //Extra menu buttons for COMBAT
        links = new ArrayList<>();
        links.add(new Link("Меню", "10000", new SetStateMenu()));
        extraLinks.put(BotState.COMBAT, links);
    }
}
