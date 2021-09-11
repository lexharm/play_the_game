package ru.home.mywizard_bot.scenario.loader;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.Illustration;
import ru.home.mywizard_bot.scenario.Item;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.actions.*;
import ru.home.mywizard_bot.scenario.checks.*;
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
        InlineLink inlineLink;
        int itemCost;
        Map<String, Item> items = new HashMap<>();
        items.put("Sword", new Item("Sword", "Обычный меч"));
        items.put("Food", new Item("Food", "Еда"));
        items.put("Gold", new Item("Gold", "Золото"));
        items.put("CaravanGold", new Item("CaravanGold", "CaravanGold", false));
        items.put("WINGED_MAN_DEAD", new Item("WINGED_MAN_DEAD", "WINGED_MAN", false));
        items.put("Rope", new Item("Rope", "Веревка"));
        items.put("Dove", new Item("Dove", "Голубь"));
        items.put("Cloak", new Item("Cloak", "Накидка"));
        items.put("FineSword", new Item("FineSword", "Хороший меч"));
        items.put("Belt", new Item("Belt", "Пояс"));
        items.put("Vial", new Item("Vial", "Флакон"));
        items.put("Armor", new Item("Armor", "Доспехи"));
        items.put("decreaseEnemyDamage", new Item("decreaseEnemyDamage", "decreaseEnemyDamage", false));
        items.put("badLuck", new Item("badLuck", "badLuck", false));

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

        id = "12";
        paragraph = new Paragraph(id, "Нетрудно было догадаться, что перекати-поле вряд ли являются мыслящими " +
                "существами. Ваша попытка не удается, а следующий шар взрывается прямо у ваших ног. Придется либо " +
                "пытаться отбиться мечом, либо постараться увернуться.");
        actions = new ArrayList<>();
        actions.add(new Event(new ModifyStrength(-4)));
        actions.add(new MovementLink("Использовать меч", "105"));
        actions.add(new MovementLink("Попытаться увернуться", "483"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "20";
        paragraph = new Paragraph(id, "Ночь приносит желанный отдых, а утром решайте, куда идти дальше.");
        actions = new ArrayList<>();
        actions.add(new Event(new ModifyStrength(2)));
        actions.add(new InlineLink("На северо-восток", "116"));
        actions.add(new InlineLink("На юго-восток", "93"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "30";
        paragraph = new Paragraph(id, "Вам повезло: от всех остальных шаров удается увернуться. Куда вы теперь направитесь?");
        actions = new ArrayList<>();
        actions.add(new InlineLink("На восток", "486"));
        actions.add(new InlineLink("На северо-восток", "110"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "59";
        itemCost = 5;
        paragraph = new Paragraph(id, "Торговец и сам не может толком рассказать, волшебный это пояс или " +
                "обыкновенный. Однако вышитые на нем руны, безусловно, содержат какую-то магическую силу. " +
                "Но вот злую или добрую? Решайте: подпоясываться или отдать покупку назад караванщику, который " +
                "согласен вернуть за нее деньги. Стоит " + itemCost + " золотых.");
        actions = new ArrayList<>();
        inlineLink = new InlineLink("Купить", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL));
        inlineLink.addEffect(new GiveItem(items.get("Belt")));
        inlineLink.addEffect(new GiveItem(items.get("CaravanGold"), itemCost));
        inlineLink.addEffect(new RemoveItem(items.get("Gold"), itemCost));
        actions.add(inlineLink);
        actions.add(new InlineLink("Назад", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL)));
        actions.add(new InlineLink("Недостаточно золота", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.LESS)));
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

        id = "93";
        paragraph = new Paragraph(id, "Когда через некоторое время вы поднимаетесь на гребень одного из барханов, " +
                "взгляду открывается следующая картина. Всего в нескольких десятках шагов на песке лежит изможденный " +
                "человек в рваной, пропитанной кровью одежде. Над ним нависает огромная крылатая тень, которая, " +
                "выпустив когти, пытается увернуться от его меча. Бой неравен, о чем ясно говорят кровоточащие " +
                "раны на теле незнакомца. Еще немного, и он будет добит этим кошмарным чудовищем. Хотите вмешаться, " +
                "или же предпочтете не участвовать в схватке?");
        actions = new ArrayList<>();
        actions.add(new MovementLink("Атаковать оружием", "480"));
        actions.add(new MovementLink("Применить Силу мысли", "515"));
        actions.add(new MovementLink("Не участвовать в схватке", "213"));
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

        id = "105";
        paragraph = new Paragraph(id, "Вы опускаете меч на перекати-поле... и раздается взрыв. Меч оплавился, но " +
                "устоял, а вот не столь прочная ваша правая рука серьезно ранена. Теперь от остальных шаров придется " +
                "уворачиваться.");
        actions = new ArrayList<>();
        actions.add(new Event(new ModifyDexterity(-1)));
        actions.add(new InlineLink("Продолжить", "483", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "110";
        paragraph = new Paragraph(id, "Ближе к вечеру навстречу вам попадается еще один обитатель пустыни. " +
                "На этот раз это хотя бы человек — глубокий старик, который безнадежно бредет по пескам, опираясь " +
                "на посох.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Заговорить", "630", true));
        actions.add(new InlineLink("Пройти мимо", "408", true));
        actions.add(new InlineLink("Атаковать телепатией", "555", true));
        actions.add(new InlineLink("Атаковать мечом", "547", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "116";
        paragraph = new Paragraph(id, "С бархана на бархан неторопливо, но верно вы продолжаете идти вперед. " +
                "Вдруг видите, что навстречу быстро (насколько это позволяет песок) несутся несколько всадников. " +
                "Свернуть в сторону уже поздно, остается только ждать их приближения. Осадив коней рядом с вами " +
                "(не правда ли, несколько странный способ передвигаться по пустыне, где за все время не встретилось " +
                "еще ни одного оазиса с водой), они спрашивают, куда вы держите путь. Судя по одежде и кривым саблям, " +
                "это кочевники. Надо что-то ответить. Но что?");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Ищу мага, обитающего в скале посреди пустыни", "409", true));
        actions.add(new InlineLink("Просто путешествую", "386", true));
        actions.add(new InlineLink("Направляюсь к гномам, живущим внутри гор Лонсам", "508", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "121";
        itemCost = 6;
        paragraph = new Paragraph(id, "Торговец говорит, что это очень мощное противоядие, которое может не раз " +
                "пригодиться в дороге. Верить ему или нет — дело ваше, но проверить это сейчас все равно невозможно. " +
                "Стоит " + itemCost + " золотых.");
        actions = new ArrayList<>();
        inlineLink = new InlineLink("Купить", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL));
        inlineLink.addEffect(new GiveItem(items.get("Vial")));
        inlineLink.addEffect(new GiveItem(items.get("CaravanGold"), itemCost));
        inlineLink.addEffect(new RemoveItem(items.get("Gold"), itemCost));
        actions.add(inlineLink);
        actions.add(new InlineLink("Назад", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL)));
        actions.add(new InlineLink("Недостаточно золота", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.LESS)));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "213";
        paragraph = new Paragraph(id, "Одним из крыльев монстр выбивает меч из рук несчастного путника и проворно " +
                "впивается когтями в его горло. После чего подхватывает свою жертву, тяжело поднимается в воздух и " +
                "вскоре скрывается из виду. Да, человечности вам не занимать, хотя, быть может, важность миссии не " +
                "позволяет вмешиваться во все схватки, свидетелем которых вы можете стать. И все же вы не выполнили " +
                "одну из главных заповедей Странников Элгариола: «Помоги — и тебе помогут». Боги накажут вас за это. " +
                "В следующий раз, когда потребуется ПРОВЕРИТЬ СВОЮ УДАЧУ - Удача от вас отвернется. А теперь решайте, " +
                "куда направиться дальше.");
        actions = new ArrayList<>();
        actions.add(new Event(new GiveItem(items.get("badLuck"))));
        actions.add(new InlineLink("На восток", "246", true));
        actions.add(new InlineLink("На юго-восток", "11", true));
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

        id = "238";
        paragraph = new Paragraph(id, "Как и обещал Майлин, первая же победа придала вам уверенности в себе и " +
                "заменила многочасовые тренировки. Теперь при желании вы сможете чувствовать, есть ли поблизости " +
                "разумная жизнь и, основываясь на этом, принимать решения.");
        actions = new ArrayList<>();
        actions.add(new Event(new ModifyThoughtPower(1)));
        actions.add(new MovementLink("Продолжить", "99"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "245";
        paragraph = new Paragraph(id, "Пройдя мимо каравана, вы уходите в ночь, а усталые люди и верблюды вскоре " +
                "скрываются из виду. Пора и вам отдохнуть. Расположившись на ночлег, быстро засыпаете, ведь завтра " +
                "предстоит нелегкий путь.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Отдыхать", "20", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "247";
        paragraph = new Paragraph(id, "Вскоре караван, уходя на север, скрывается из виду, а вы продолжаете свой путь.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Продолжить", "93"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "250";
        paragraph = new Paragraph(id, "Торговец настолько любезен, что укладывает вас спать прямо в своем шатре, не взяв за это ни гроша.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Отдыхать", "413", new InventoryCheck(items.get("CaravanGold"), 8, Condition.MORE_EQUAL), true));
        actions.add(new InlineLink("Отдыхать", "540", new InventoryCheck(items.get("CaravanGold"), 8, Condition.LESS), true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "254";
        paragraph = new Paragraph(id, "Но попытка разрубить это странное перекати-поле оказывается еще более " +
                "неудачной. Снова взрыв — на этот раз меч устоял, а вот ваша правая рука серьезно ранена. " +
                "Теперь не остается ничего другого, кроме как уворачиваться от остальных шаров.");
        actions = new ArrayList<>();
        actions.add(new Event(new ModifyDexterity(-1)));
        actions.add(new InlineLink("Попытаться увернуться", "483"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "259";
        itemCost = 2;
        paragraph = new Paragraph(id, "Моток веревки, которая всегда может пригодиться. Стоит " + itemCost + " золотых.");
        actions = new ArrayList<>();
        inlineLink = new InlineLink("Купить", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL));
        inlineLink.addEffect(new GiveItem(items.get("Rope")));
        inlineLink.addEffect(new GiveItem(items.get("CaravanGold"), itemCost));
        inlineLink.addEffect(new RemoveItem(items.get("Gold"), itemCost));
        actions.add(inlineLink);
        actions.add(new InlineLink("Назад", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL)));
        actions.add(new InlineLink("Недостаточно золота", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.LESS)));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "278";
        itemCost = 2;
        paragraph = new Paragraph(id, "В бою эти доспехи защитят вас, и урон, наносимый противником станет на 1 меньше. " +
                "Но, как это ни печально, они отнимут у вас 2 единицы ЛОВКОСТИ. К тому же путешествие по пустыне пешком " +
                "да еще в рыцарских доспехах обещает быть весьма «приятным». Каждый прожитый день в доспехах будет " +
                "стоить 3 СИЛЫ — именно на столько латы осложнят вам жизнь. Так что решайте, стоит ли тащить " +
                "покупку на себе. Цена " + itemCost + " золотых.");
        actions = new ArrayList<>();
        inlineLink = new InlineLink("Купить", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL));
        inlineLink.addEffect(new GiveItem(items.get("Armor")));
        inlineLink.addEffect(new GiveItem(items.get("CaravanGold"), itemCost));
        inlineLink.addEffect(new RemoveItem(items.get("Gold"), itemCost));
        inlineLink.addEffect(new ModifyDexterity(-2));
        inlineLink.addEffect(new GiveItem(items.get("decreaseEnemyDamage")));
        actions.add(inlineLink);
        actions.add(new InlineLink("Назад", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL)));
        actions.add(new InlineLink("Недостаточно золота", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.LESS)));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "290";
        itemCost = 3;
        paragraph = new Paragraph(id, "Накидка, по словам торговца, при случае сможет защитить от песчаной бури, " +
                "а быть может, и не только от нее. Стоит " + itemCost + " золотых.");
        actions = new ArrayList<>();
        inlineLink = new InlineLink("Купить", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL));
        inlineLink.addEffect(new GiveItem(items.get("Cloak")));
        inlineLink.addEffect(new GiveItem(items.get("CaravanGold"), itemCost));
        inlineLink.addEffect(new RemoveItem(items.get("Gold"), itemCost));
        actions.add(inlineLink);
        actions.add(new InlineLink("Назад", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL)));
        actions.add(new InlineLink("Недостаточно золота", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.LESS)));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "344";
        paragraph = new Paragraph(id, "Первый шар, который должен был прокатиться в нескольких шагах от вас, резко " +
                "поворачивает и устремляется в вашу сторону. Попытаетесь разрубить его мечом или посмотрите, что " +
                "будет дальше?");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Использовать меч", "105", true));
        actions.add(new InlineLink("Ожидать", "496", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "345";
        paragraph = new Paragraph(id, "Нельзя сказать, чтобы погонщик был особенно рад вашему появлению. Если не " +
                "получится завести разговор, тогда придётся по-хорошему уйти.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Проверить обаяние", "346", new LuckCheck("629", "245"), true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "359";
        paragraph = new Paragraph(id, "К сожалению, проворства вам явно не хватает. Отпрыгивая от одного шара, вы " +
                "попадаете прямо на другой, взрыв следует за взрывом. Кто бы ни послал навстречу вам эти " +
                "перекати-поле, он рассчитал правильно. Ваш труп так и останется в самом начале пути...");
        actions = new ArrayList<>();
        actions.add(new Event(new EndGame()));
        actions.add(new MovementLink("Начать заново", "newGameConfirm"));
        actions.add(new MovementLink("Выйти в главное меню", "mainMenu"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "376";
        itemCost = 5;
        paragraph = new Paragraph(id, "Рана, нанесенная этим мечом, будет стоить врагу не 2, а 3 СИЛЫ. Стоит " + itemCost + " золотых.");
        actions = new ArrayList<>();
        inlineLink = new InlineLink("Купить", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL));
        inlineLink.addEffect(new GiveItem(items.get("FineSword")));
        inlineLink.addEffect(new GiveItem(items.get("CaravanGold"), itemCost));
        inlineLink.addEffect(new RemoveItem(items.get("Gold"), itemCost));
        inlineLink.addEffect(new SetDamage(3));
        actions.add(inlineLink);
        actions.add(new InlineLink("Назад", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL)));
        actions.add(new InlineLink("Недостаточно золота", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.LESS)));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "413";
        paragraph = new Paragraph(id, "Более того, он говорит, что ему редко удается встретить столь любезного и " +
                "приятного покупателя, а жизнь караванщика достаточно скучна. Поэтому он предлагает отметить ваши " +
                "покупки ужином да заодно выпить немного вина за удачу в путешествиях и в торговле. Откажетесь, " +
                "сославшись на усталость, или согласитесь?");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Отказаться", "38", true));
        actions.add(new InlineLink("Согласиться", "133", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "421";
        itemCost = 5;
        paragraph = new Paragraph(id, "Вы сможете выпустить птицу на волю и голубь будет сопровождать вас. Когда " +
                "потребуется, вы сможете отправить его с посланием в Элгариол. Стоит " + itemCost + " золотых.");
        actions = new ArrayList<>();
        inlineLink = new InlineLink("Купить", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL));
        inlineLink.addEffect(new GiveItem(items.get("Dove")));
        inlineLink.addEffect(new GiveItem(items.get("CaravanGold"), itemCost));
        inlineLink.addEffect(new RemoveItem(items.get("Gold"), itemCost));
        actions.add(inlineLink);
        actions.add(new InlineLink("Назад", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.MORE_EQUAL)));
        actions.add(new InlineLink("Недостаточно золота", "512", new InventoryCheck(items.get("Gold"), itemCost, Condition.LESS)));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "446";
        paragraph = new Paragraph(id, "Простившись с торговцем, устремляетесь дальше в ночь. И только отойдя на " +
                "достаточно безопасное расстояние от каравана, решаетесь остановиться на ночлег.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Отдыхать", "20", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "479";
        paragraph = new Paragraph(id, "Перекати-поле приближаются. Неожиданно первый шар, который должен был " +
                "прокатиться в нескольких шагах от вас, резко сворачивает и, натолкнувшись на ногу, взрывается. " +
                "Попытаетесь теперь увернуться от остальных, обнажите меч или воспользуетесь своей Силой мысли?");
        actions = new ArrayList<>();
        actions.add(new Event(new ModifyStrength(-4)));
        actions.add(new InlineLink("Попытаться увернуться", "483", true));
        actions.add(new InlineLink("Обножить меч", "254", true));
        actions.add(new InlineLink("Использовать Силу мысли", "12", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "480";
        paragraph = new Paragraph(id, "Обнажив меч, издаете воинственный клич, чем сразу же привлекаете " +
                "внимание твари. Несколько раз взмахнув огромными крыльями, она оказывается прямо над вами, выпустив " +
                "свою жертву. Теперь вы можете ее разглядеть. Между двух больших кожистых крыльев, как у летучей " +
                "мыши, виднеется человеческое тело!");
        actions = new ArrayList<>();
        actions.add(new Event(new ModifyDexterity(-1)));
        actions.add(new Event(new SetStateCombat()));
        actions.add(new Event(new AddCombatCheck()));
        actions.add(new Enemy("КРЫЛАТЫЙ ЧЕЛОВЕК", "WINGED_MAN", 10, 9, 0, 3));
        inlineLink = new InlineLink("Продолжить", "99", new EnemyDead(), new GiveItem(items.get("WINGED_MAN_DEAD")), true);
        inlineLink.addEffect(new ModifyDexterity(1));
        actions.add(inlineLink);
        actions.add(new MovementLink("Путешествие окончено" + Emojis.SCULL_BONES, "combatDefeat", new PlayerDead()));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "481";
        paragraph = new Paragraph(id, "Теперь вы идете параллельно с караваном, который и не думает куда-нибудь " +
                "сворачивать. Попробуете обогнуть его с юга или все же пойдете по направлению к верблюдам?");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Попытаться обойти", "247"));
        actions.add(new InlineLink("Подойти к верблюдам", "96"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "483";
        paragraph = new Paragraph(id, "ПРОВЕРЬТЕ СВОЮ УДАЧУ.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Проверить удачу", "30", new LuckCheck("30", "359"), true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "486";
        paragraph = new Paragraph(id, "Путь настолько тяжел, что вас даже не слишком изумляет силуэт, появившийся " +
                "через несколько сот шагов на гребне бархана. Вначале его можно принять за мираж, неясное видение, " +
                "настолько неожиданно появление в этих раскаленных песках рыцаря в полном вооружении, да еще и на " +
                "коне. Пот стекает по его лицу под поднятым забралом, но он тем не менее не расстается с тяжелыми " +
                "латами, хотя не совсем понятно, от кого они могут защитить в этих местах. Ведь наибольшую опасность " +
                "здесь представляет отнюдь не клинок, а магия и телепатия.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Воспользоваться телепатией", "360", new ThoughtPowerCheck(5, Condition.MORE_EQUAL), true));
        actions.add(new InlineLink("Продолжить", "252", true));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "496";
        paragraph = new Paragraph(id, "Шар натыкается на вашу ногу... и раздается взрыв. Попытаетесь теперь " +
                "увернуться от остальных, разрубите следующий шар мечом или воспользуетесь своей Силой мысли?");
        actions = new ArrayList<>();
        actions.add(new Event(new ModifyStrength(-4)));
        actions.add(new InlineLink("Попытаться увернуться", "483"));
        actions.add(new InlineLink("Использовать меч", "254"));
        actions.add(new InlineLink("Использовать Силу мысли", "12"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "512";
        paragraph = new Paragraph(id, "Торговец дружелюбен и с удовольствием показывает товары в своем шатре, " +
                "который слуги уже успели поставить. Можете купить у него все, что приглянется, — были бы деньги. " +
                "А товары такие (в скобках указаны цены):\n" +
                "моток веревки (2);\n" +
                "почтовый голубь в изящной позолоченной клетке (5);\n" +
                "накидка от песчаной бури (3);\n" +
                "меч, на рукояти которого неизвестные вам письмена вязью (5);\n" +
                "шелковый пояс (5);\n" +
                "флакон с неизвестной жидкостью (6);\n" +
                "рыцарские доспехи (2).\n" +
                "После этого можете попрощаться и пойти дальше, решая переночевать одному, либо попроситься на " +
                "ночлег к любезному торговцу.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Веревка", "259", new InventoryCheck(items.get("Rope"), false)));
        actions.add(new InlineLink("Голубь", "421", new InventoryCheck(items.get("Dove"), false)));
        actions.add(new InlineLink("Накидка", "290", new InventoryCheck(items.get("Cloak"), false)));
        actions.add(new InlineLink("Меч", "376", new InventoryCheck(items.get("FineSword"), false)));
        actions.add(new InlineLink("Пояс", "59", new InventoryCheck(items.get("Belt"), false)));
        actions.add(new InlineLink("Флакон", "121", new InventoryCheck(items.get("Vial"), false)));
        actions.add(new InlineLink("Доспехи", "278", new InventoryCheck(items.get("Armor"), false)));
        actions.add(new InlineLink("Попрощаться с торговцем", "446"));
        actions.add(new InlineLink("Попроситься на ночлег", "250"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "514";
        paragraph = new Paragraph(id, "Какое-то предчувствие заставляет вас не только свернуть, но и ускорить шаг. " +
                "Перекати-поле проносятся мимо, а вы продолжаете свой путь на северо-восток.");
        actions = new ArrayList<>();
        actions.add(new InlineLink("Продолжить", "110"));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "515";
        paragraph = new Paragraph(id, "Вы концентрируетесь и наносите мысленный удар. Почувствовав боль, тварь " +
                "взмывает в воздух, и теперь становится видно, что между огромными кожистыми крыльями, как у летучей " +
                "мыши, у нее небольшое человеческое тело с лапами вместо ног.");
        actions = new ArrayList<>();
        actions.add(new Event(new SetStateCombat()));
        actions.add(new Event(new AddCombatCheck()));
        actions.add(new Enemy("КРЫЛАТЫЙ ЧЕЛОВЕК", "WINGED_MAN", 10, 0, 3));
        actions.add(new MovementLink("Продолжить", "238", new EnemyDead(), new GiveItem(items.get("WINGED_MAN_DEAD"))));
        actions.add(new MovementLink("Путешествие окончено" + Emojis.SCULL_BONES, "combatDefeat", new PlayerDead()));
        paragraph.setActions(actions);
        allParagraphs.put(id, paragraph);

        id = "540";
        paragraph = new Paragraph(id, "Ночь проходит спокойно. Утром вы прощаетесь с доброжелательным " +
                "караванщиком и спрашиваете его, как лучше всего пересечь Мортлэндские топи. Он отвечает, что " +
                "ни разу не пытался попасть на ту сторону болот или гор, но слышал, что где-то неподалеку есть " +
                "жилье. Сейчас надо будет пройти немного на юго-восток, потом прямо на восток — вот и выйдите к топям. " +
                "Послушаетесь совета и направитесь на юго-восток или решите, что торговец хочет заманить вас в " +
                "ловушку, и пойдете на северо-восток?");
        actions = new ArrayList<>();
        actions.add(new Event(new ModifyStrength(3)));
        actions.add(new InlineLink("Юго-восток", "93", true));
        actions.add(new InlineLink("Северо-восток", "116", true));
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
    }
}
