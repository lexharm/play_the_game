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
    SHIELD(EmojiParser.parseToUnicode(":shield:"));

    private String emojiName;

    @Override
    public String toString() {
        return emojiName;
    }
}
