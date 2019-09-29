package com.timgapps.tds_game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.timgapps.tds_game.levels.Level;

public class DynamicObject extends GameObject {
    private final float MAX_VELOCITY = 10;

    public DynamicObject(Level level, Vector2 pos, float rotation, int objectType, TextureRegion textureRegion) {
        super(level, pos, rotation, objectType, textureRegion);
    }

}
