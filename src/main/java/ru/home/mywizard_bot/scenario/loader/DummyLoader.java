package ru.home.mywizard_bot.scenario.loader;

import org.springframework.stereotype.Component;
import ru.home.mywizard_bot.botapi.BotState;
import ru.home.mywizard_bot.scenario.Enemy;
import ru.home.mywizard_bot.scenario.Item;
import ru.home.mywizard_bot.scenario.Link;
import ru.home.mywizard_bot.scenario.Paragraph;
import ru.home.mywizard_bot.scenario.checks.Check;
import ru.home.mywizard_bot.scenario.checks.EventCheck;
import ru.home.mywizard_bot.scenario.checks.GameAlreadyExists;
import ru.home.mywizard_bot.scenario.features.*;

import java.util.ArrayList;
import java.util.List;

@Component
public class DummyLoader extends Loader {
    @Override
    public void loadExtraLinks() {
        List<Link> links;

        //Extra menu buttons for SHOW_MAIN_MENU
        links = new ArrayList<>();
        extraLinks.put(BotState.SHOW_MAIN_MENU, links);

        //Extra menu buttons for PLAY_SCENARIO
        links = new ArrayList<>();
        links.add(new Link("Листок путешественника", "9000", new SetStateMenu()));
        links.add(new Link("Меню", "10000", new SetStateMenu()));
        extraLinks.put(BotState.PLAY_SCENARIO, links);

        //Extra menu buttons for COMBAT
        links = new ArrayList<>();
        links.add(new Link("Нанести удар!", "10000"));
        links.add(new Link("Меню", "10000", new SetStateMenu()));
        extraLinks.put(BotState.COMBAT, links);
    }

    @Override
    public void loadParagraphs() {
        Paragraph paragraph;
        List<Link> links;
        String id;
        Enemy enemy;
        Check check;

        id = "-10000";
        paragraph = new Paragraph(id, "Раздел отсутствует :(");
        links = new ArrayList<>();
        links.add(new Link("Вернуться в главное меню", "10000"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        // Inventory
        id = "9000";
        paragraph = new Paragraph(id, "");
        links = new ArrayList<>();
        links.add(new Link("Восстановить здоровье едой", "9000"));
        links.add(new Link("Описание телепатических способностей", "9000"));
        links.add(new Link("Вернуться в игру", "9000", new ReturnToGame()));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "9999";
        paragraph = new Paragraph(id, "Добро пожаловать в игру \"Повелитель безбрежной пустыни\" от PLAY_the_GAME!\n" +
                "Для ознакомления с правилами игры, рекомендуем ознакомиться с Руководством.");
        links = new ArrayList<>();
        links.add(new Link("Руководство", "10003"));
        links.add(new Link("Новая игра", "10002"));
        links.add(new Link("Статистика", "10004"));
        links.add(new Link("Подробнее о боте", "10005"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "10000";
        paragraph = new Paragraph(id, "Главное меню");
        links = new ArrayList<>();
        links.add(new Link("Вернуться в игру", "10001", new GameAlreadyExists(), new ReturnToGame()));
        links.add(new Link("Новая игра", "10002"));
        links.add(new Link("Руководство", "10003"));
        links.add(new Link("Статистика", "10004"));
        links.add(new Link("Подробнее о боте", "10005"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "10002";
        paragraph = new Paragraph(id, "Вы уверены?");
        links = new ArrayList<>();
        links.add(new Link("Да", "0"));
        links.add(new Link("Назад", "10000"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "10003";
        paragraph = new Paragraph(id, "Выберите интересующую тему");
        links = new ArrayList<>();
        links.add(new Link("Правила игры", "10008"));
        links.add(new Link("Ловкость и сила", "10009"));
        links.add(new Link("Удача", "10010"));
        links.add(new Link("Битвы", "10011"));
        links.add(new Link("Ранения", "10012"));
        links.add(new Link("Сила мысли", "10013"));
        links.add(new Link("Если вы проголодались", "10014"));
        links.add(new Link("Листок путешественника", "10015"));
        links.add(new Link("Назад", "10000"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "10004";
        paragraph = new Paragraph(id, "Выберите раздел");
        links = new ArrayList<>();
        links.add(new Link("Персональная статистика", "10016"));
        links.add(new Link("Глобальная статистика", "10017"));
        links.add(new Link("Назад", "10000"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "10005";
        paragraph = new Paragraph(id, "Выберите раздел");
        links = new ArrayList<>();
        links.add(new Link("О сценарии", "10018"));
        links.add(new Link("О разработчиках", "10019"));
        links.add(new Link("Назад", "10000"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "-1";
        paragraph = new Paragraph(id, "Внезапно из-за спазма в горле вы давитесь собственной слюной и умираете " +
                "так и не достигнув цели своего путешествия.");
        paragraph.addFeature(new EndGame());
        links = new ArrayList<>();
        links.add(new Link("Начать заново", "1"));
        links.add(new Link("Выйти в главное меню", "10000"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "0";
        paragraph = new Paragraph(id, "AN IMMORTALIS ES?");
        links = new ArrayList<>();
        links.add(new Link("EX ANIMO, FRATER!", "1", new ReturnToGame()));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "1";
        paragraph = new Paragraph(id, "Плодородные земли Элгариола позади. Перед вами пустыня. Вряд ли удастся " +
                "обнаружить здесь тропинку, а тем более какую-нибудь дорогу, так что придется ориентироваться  только " +
                "по солнцу. Одинокая гора должна быть где-то на востоке, но стоит ли отправляться туда сразу?  В " +
                "деревушке, раскинувшейся у самого края пустыни, вы узнали, что древний караванный путь уходит на " +
                "юго-восток. Там могут даже встретиться еще не пересохшие оазисы. Путь на северо-восток ведет к горам " +
                "Лонсам. Куда направитесь вы?");
        paragraph.addFeature(new ReturnToGame());
        paragraph.addFeature(new SetPlayerStrength(10));
        paragraph.addFeature(new GiveItem(new Item("Sword", "Меч-хладинец")));
        links = new ArrayList<>();
        links.add(new Link("На северо-восток", "89", new EventCheck("Mayline_dead")));
        links.add(new Link("На юго-восток", "230"));
        Link link = new Link("Вернуться и подраться с Майлином", "999", new EventCheck("Mayline_dead", false));
        link.addCheck(new EventCheck("Mayline_laugh", false));
        links.add(link);
        link = new Link("Майлин смеялся при виде ваших пяток. Нужно ему отомстить!", "999", new EventCheck("Mayline_laugh"));
        link.addCheck(new EventCheck("Mayline_dead", false));
        links.add(link);
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "999";
        paragraph = new Paragraph(id, "Ах, так!? - вскрикивает маг и хватается за посох. Не ждите пощады!");
        links = new ArrayList<>();
        links.add(new Link("Трусливо сбежать", "1", new GiveItem(new Item("Mayline_laugh", "Mayline_laugh", false))));
        paragraph.setLinks(links);
        enemy = new Enemy("Майлин", "Mayline", 10, 12, 25);
        paragraph.setEnemy(enemy);
        paragraph.setCombat(true);
        allParagraphs.put(id, paragraph);

        id = "1000";
        paragraph = new Paragraph(id, "Победа! Теперь можно никуда не ходить.");
        links = new ArrayList<>();
        links.add(new Link("Начать заново", "1", new GiveItem(new Item("Mayline_dead", "Mayline_dead", false))));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "-2";
        paragraph = new Paragraph(id, "Слабак! Тряпка! Дешёвка! Ты мёртв! - говорит Майлин скармливая ваш труп " +
                "кондорам. Однако во всём есть свои плюсы - теперь вам не нужно идти через пустыню :)");
        links = new ArrayList<>();
        links.add(new Link("Начать заново", "1"));
        paragraph.setLinks(links);
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
        links = new ArrayList<>();
        links.add(new Link("Свернуть в сторону", "514"));
        links.add(new Link("Продолжать путь", "479"));
        links.add(new Link("Остановиться и обножить меч", "344"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "230";
        paragraph = new Paragraph(id, "ВНЕЗАПНО вы находите монету с изображением профиля Императора! Это к удаче!" +
                " Путешествие по пустыне оказывается совсем не таким приятным, как это могло " +
                "показаться в королевском дворце в Элгариоле. С непривычки ноги вязнут в песке, пот заливает глаза, " +
                "пустыня давит и окружает со всех сторон. А ведь еще предстоит пробраться через Мортлэндские топи, а " +
                "еще... Лучше уж об этом не думать. Тем более что впереди что-то виднеется. Через полчаса становится " +
                "понятно, что это караван верблюдов, неспешно двигающийся с юга наперерез вам. Ваше действие?");
        paragraph.addFeature(new GiveItem(new Item("Coin", "Монета с профилем Императора")));
        links = new ArrayList<>();
        links.add(new Link("Свернуть на север, чтобы избежать встречи с ним", "481"));
        links.add(new Link("Идти дальше", "96"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "96";
        paragraph = new Paragraph(id, "Часа через полтора вы догоняете караван. Погонщик первого верблюда " +
                "выкрикивает какое-то протяжное слово на неизвестном языке, и все животные останавливаются. Смуглые " +
                "худощавые люди спрыгивают на землю. Одни начинают расседлывать усталых животных, другие разбивают " +
                "шатры. На вас никто не обращает внимания. Смеркается, и в самом деле пора подумать о ночлеге. " +
                "Хотите подойти поговорить с одним из погонщиков или продолжите свой путь, решив, что ночь безопасней " +
                "провести в одиночку, чем с незнакомыми людьми?");
        links = new ArrayList<>();
        links.add(new Link("Поговорить с погонщиком", "345"));
        links.add(new Link("Продолжить путь", "245"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "245";
        paragraph = new Paragraph(id, "Пройдя мимо каравана, вы уходите в ночь, а усталые люди и верблюды вскоре " +
                "скрываются из виду. Пора и вам отдохнуть. Расположившись на ночлег, быстро засыпаете, ведь завтра " +
                "предстоит нелегкий путь");
        links = new ArrayList<>();
        links.add(new Link("Отдыхать", "20"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = "20";
        paragraph = new Paragraph(id, "Ночь приносит желанный отдых (можете восстановить 2 СИЛЫ), а утром решайте, " +
                "куда идти дальше.");
        links = new ArrayList<>();
        links.add(new Link("На северо-восток", "116"));
        links.add(new Link("На юго-восток", "93"));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);
    }
}
