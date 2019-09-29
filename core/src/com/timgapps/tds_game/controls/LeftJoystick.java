package com.timgapps.tds_game.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.timgapps.tds_game.Tds;
import com.timgapps.tds_game.levels.Level;

public class LeftJoystick extends Touchpad {

    private Level level;
    private float x, y;

    private Touchpad touchpad;

    private float touchpadWidth = 200;
    private float touchpadHeight = 200;
    private TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;

    public LeftJoystick(float deadzoneRadius, Skin skin) {
        super(deadzoneRadius, skin);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

//        System.out.println("knob" + touchpad.getKnobX());
//        if (touchpad.isTouched()) System.out.println("dsfdfsdfsdfs");
    }
}
