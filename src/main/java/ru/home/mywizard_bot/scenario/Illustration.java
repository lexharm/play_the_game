package ru.home.mywizard_bot.scenario;

import lombok.Getter;

@Getter
public class Illustration {
    private String caption;
    private String imagePath;

    public Illustration(String caption, String imagePath) {
        this.caption = caption;
        this.imagePath = imagePath;
    }
}
