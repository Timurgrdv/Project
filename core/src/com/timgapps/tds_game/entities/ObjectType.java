package com.timgapps.tds_game.entities;

import com.timgapps.tds_game.Tds;

import javax.swing.text.html.parser.Entity;

public enum ObjectType {

    PLAYER("player"),
    BULLET("bullet"),
    PILL("pill"),
    AMMO("ammo"),
    ENEMY("enemy");

    private String id;
    private int width, height;

    private ObjectType(String id) {
        this.id = id;
    }

    public String getId() {
        return  id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
