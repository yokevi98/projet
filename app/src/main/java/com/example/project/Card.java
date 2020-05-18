package com.example.project;

import java.util.List;

public class Card {
    private Integer id;
    private String name;
    private String type;
    private String desc;
    private String race;
    private String archetype;
    private List<Card_sets> card_sets;
    private List<Card_images> card_images;
    private List<Card_prices> card_prices;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getRace() {
        return race;
    }

    public String getArchetype() {
        return archetype;
    }

    public List<Card_sets> getCard_sets() {
        return card_sets;
    }

    public List<Card_images> getCard_images() {
        return card_images;
    }

    public List<Card_prices> getCard_prices() {
        return card_prices;
    }
}
