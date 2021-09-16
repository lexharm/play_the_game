package ru.home.mywizard_bot.utils;

import com.vdurmont.emoji.EmojiParser;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Emojis {
    SPARKLES(EmojiParser.parseToUnicode(":sparkles:")),
    SCROLL(EmojiParser.parseToUnicode(":scroll:")),
    SWORDS(EmojiParser.parseToUnicode(":crossed_swords:")),
    BALL(EmojiParser.parseToUnicode(":crystall_ball:")),
    DICE(EmojiParser.parseToUnicode(":game_die:")),
    SHIELD(EmojiParser.parseToUnicode(":shield:")),
    HEART(EmojiParser.parseToUnicode(":heart:")),
    BROKEN_HEART(EmojiParser.parseToUnicode(":broken_heart:")),
    DAGGER(EmojiParser.parseToUnicode(":dagger_knife:")),
    SKELETON(EmojiParser.parseToUnicode(":skull:")),
    SCULL_BONES(EmojiParser.parseToUnicode(":skull_crossbones:")),
    THUMBSUP(EmojiParser.parseToUnicode(":+1:")),
    THUMBSDOWN(EmojiParser.parseToUnicode(":-1:")),
    EXCLAMATION(EmojiParser.parseToUnicode(":exclamation:"));

    private String emojiName;

    @Override
    public String toString() {
        return emojiName;
    }
}
