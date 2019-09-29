package com.timgapps.tds_game.levels;

import com.timgapps.tds_game.entities.GameObject;
import com.timgapps.tds_game.entities.Player;

import java.util.ArrayList;

public abstract class Map {
    protected ArrayList<GameObject> objects;

    public static Player player;

    public Map() {
        objects = new ArrayList<GameObject>();
//        player = new Player(100, 100, this);

//        objects.add(player);
    }


}
