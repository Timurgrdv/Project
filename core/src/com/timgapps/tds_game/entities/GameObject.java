package com.timgapps.tds_game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.timgapps.tds_game.levels.Level;

import org.w3c.dom.css.Rect;

public class GameObject extends Actor {
    public final Vector2 position;
    private TextureRegion textureRegion;
    private Level level;
    public final float rotaion;
    public Rectangle bounds;
//    public final Rectangle bounds;

    public static final int TREE = 0;
    public static final int BRICK = 1;
    public static final int PLAYER_BULLET = 2;
    public static final int ENEMY_BULLET = 3;
    public static final int PLAYER = 4;

    private int objectType;

    public GameObject(Level level, Vector2 position, float rotaion, int objectType, TextureRegion textureRegion) {

        this.level = level;
        this.position = position;
        this.rotaion = rotaion;
        this.textureRegion = textureRegion;
        this.objectType = objectType;

        setPosition(position.x, position.y);
        setRotation(rotaion);
        bounds = new Rectangle(position.x, position.y, textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
    }

    public boolean checkCollision(Rectangle rectangle) {
        return rectangle.overlaps(bounds);
    }

    @Override
    public float getWidth() {
        return super.getWidth();
    }

    @Override
    public float getHeight() {
        return super.getHeight();
    }

    public int getObjectType() {
        return objectType;
    }


}

