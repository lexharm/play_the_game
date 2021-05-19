package ru.home.mywizard_bot.scenario;

import lombok.Getter;

@Getter
public class Item {
    private String id;
    private String name;
    private boolean visible;
    private int size;

    // Default constructor
    public Item(String id, String name) {
        this.id = id;
        this.name = name;
        visible = true;
        size = 1;
    }

    // Constructor for invisible items (like some checks)
    public Item(String id, String name, boolean visible) {
        this(id, name);
        this.visible = visible;
        if (!visible)
            size = 0;
    }

    // Constructor for custom size item
    public Item(String id, String name, int size) {
        this(id, name);
        this.size = size;
        visible = true;
    }
}
