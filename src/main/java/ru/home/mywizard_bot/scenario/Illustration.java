package ru.home.mywizard_bot.scenario;

import lombok.Getter;

import java.io.File;

@Getter
public class Illustration implements Cloneable {
    private String caption;
    private String imagePath;

    public Illustration(String caption, String imagePath) {
        this.caption = caption;
        this.imagePath = File.separator + "app" + File.separator + "src" + File.separator + "main" + File.separator +
                "resources" + File.separator + "static" + File.separator + "images" + File.separator + imagePath;
    }
}
