package ru.home.mywizard_bot.scenario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Story {
    private static Story story = new Story();
    private boolean isInitialized = false;
    private Map<Integer, Paragraph> allParagraphs = new HashMap<>();

    private Story() {
        this.init();
    }

    public static Story getInstance() {
        if (!story.isInitialized) {
            story.isInitialized = true;
            story.init();
        }
        return story;
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

    public void init() {
        Paragraph paragraph;
        List<Link> links;
        int id;

        id = -1;
        paragraph = new Paragraph(id, "Внезапно из-за спазма в горле вы давитесь собственной слюной и умираете " +
                "так и не достигнув цели своего путешествия.");
        links = new ArrayList<>();
        links.add(new Link("Начать заново", 1));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = 1;
        paragraph = new Paragraph(id, "Плодородные земли Элгариола позади. Перед вами пустыня. Вряд ли удастся " +
                "обнаружить здесь тропинку, а тем более какую-нибудь дорогу, так что придется ориентироваться  только " +
                "по солнцу. Одинокая гора должна быть где-то на востоке, но стоит ли отправляться туда сразу?  В " +
                "деревушке, раскинувшейся у самого края пустыни, вы узнали, что древний караванный путь уходит на " +
                "юго-восток. Там могут даже встретиться еще не пересохшие оазисы. Путь на северо-восток ведет к горам " +
                "Лонсам. Куда направитесь вы?");
        links = new ArrayList<>();
        links.add(new Link("На северо-восток", 89));
        links.add(new Link("На юго-восток", 230));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = 89;
        paragraph = new Paragraph(id, "Решив, что хребет Лонсам все же предпочтительнее Мортлэндских болот, " +
                "направляетесь на северо-восток. К полудню солнце печет настолько сильно, что вы уже еле-еле " +
                "передвигаете ноги, а ведь это только первый день пути. К тому же глаза сильно устают от бескрайнего " +
                "моря песка, да и идти по пустыне гораздо труднее, чем по ровной и, главное, твердой земле. Вы " +
                "начинаете мечтать об отдыхе, но вокруг нет даже намека на оазис, а опускаться на раскаленный песок " +
                "под полуденным солнцем вряд ли имеет смысл. Поднимается ветер (хорошо бы еще не было песчаной бури). " +
                "Но вместе с ветром появляется и первая растительность — несколько шаров перекати-поле, быстро " +
                "катящихся навстречу. Что вы предпримите?");
        links = new ArrayList<>();
        links.add(new Link("Свернуть в сторону", 514));
        links.add(new Link("Продолжать путь", 479));
        links.add(new Link("Остановиться и обножить меч", 344));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = 230;
        paragraph = new Paragraph(id, "Путешествие по пустыне оказывается совсем не таким приятным, как это могло " +
                "показаться в королевском дворце в Элгариоле. С непривычки ноги вязнут в песке, пот заливает глаза, " +
                "пустыня давит и окружает со всех сторон. А ведь еще предстоит пробраться через Мортлэндские топи, а " +
                "еще... Лучше уж об этом не думать. Тем более что впереди что-то виднеется. Через полчаса становится " +
                "понятно, что это караван верблюдов, неспешно двигающийся с юга наперерез вам. Ваше действие?");
        links = new ArrayList<>();
        links.add(new Link("Свернуть на север, чтобы избежать встречи с ним", 481));
        links.add(new Link("Идти дальше", 96));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = 96;
        paragraph = new Paragraph(id, "Часа через полтора вы догоняете караван. Погонщик первого верблюда " +
                "выкрикивает какое-то протяжное слово на неизвестном языке, и все животные останавливаются. Смуглые " +
                "худощавые люди спрыгивают на землю. Одни начинают расседлывать усталых животных, другие разбивают " +
                "шатры. На вас никто не обращает внимания. Смеркается, и в самом деле пора подумать о ночлеге. " +
                "Хотите подойти поговорить с одним из погонщиков или продолжите свой путь, решив, что ночь безопасней " +
                "провести в одиночку, чем с незнакомыми людьми?");
        links = new ArrayList<>();
        links.add(new Link("Поговорить с погонщиком", 345));
        links.add(new Link("Продолжить путь", 245));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = 245;
        paragraph = new Paragraph(id, "Пройдя мимо каравана, вы уходите в ночь, а усталые люди и верблюды вскоре " +
                "скрываются из виду. Пора и вам отдохнуть. Расположившись на ночлег, быстро засыпаете, ведь завтра " +
                "предстоит нелегкий путь");
        links = new ArrayList<>();
        links.add(new Link("Отдыхать", 20));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);

        id = 20;
        paragraph = new Paragraph(id, "Ночь приносит желанный отдых (можете восстановить 2 СИЛЫ), а утром решайте, " +
                "куда идти дальше.");
        links = new ArrayList<>();
        links.add(new Link("На северо-восток", 116));
        links.add(new Link("На юго-восток", 93));
        paragraph.setLinks(links);
        allParagraphs.put(id, paragraph);
    }
}
