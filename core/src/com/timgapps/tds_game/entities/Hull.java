package com.timgapps.tds_game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.timgapps.tds_game.levels.Level;

public class Hull extends GameObject {
    private TextureRegion image;

    public Hull(Level level, Vector2 pos, float rotaion, int objectType, TextureRegion textureRegion) {
        super(level, pos, rotaion, objectType, textureRegion);
        image = textureRegion;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(image, getX(), getY(), Player.HULL_WIDTH / 2, Player.HULL_HEIGHT / 2,
                image.getRegionWidth(), image.getRegionHeight(), 1, 1, getRotation());
    }
}

