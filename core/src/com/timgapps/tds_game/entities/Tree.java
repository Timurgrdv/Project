package com.timgapps.tds_game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.timgapps.tds_game.levels.Level;


public class Tree extends GameObject {

    private Rectangle bounds;
//    private Circle bounds;
    private TextureRegion image;

    public Tree(Level level, Vector2 position, float rotaion, int objectType, TextureRegion textureRegion) {
        super(level, position, rotaion, objectType, textureRegion);
        bounds = new Rectangle(position.x + 12, position.y + 12, textureRegion.getRegionWidth() - 24, textureRegion.getRegionHeight() - 24);
//        bounds = new Circle(position.x, position.y, textureRegion.getRegionWidth() / 2);
        image = textureRegion;
        setPosition(position.x, position.y);
        level.addChild(this);
    }

    public Rectangle getBounds() {
        return bounds;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
//        batch.draw(image, getX(), getY(), image.getRegionWidth() / 2, image.getRegionHeight() / 2,
//                image.getRegionWidth(), image.getRegionHeight(), 1, 1, getRotation());

        batch.draw(image, getX(), getY());
    }
}
