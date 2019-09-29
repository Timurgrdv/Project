package com.timgapps.tds_game.controls;

import com.badlogic.gdx.math.Vector2;

public class Circle {

    public Vector2 pos;
    private float R;
    public Circle(Vector2 pos, float R) {
        this.pos = new Vector2(pos);
        this.R = R;
    }

    public boolean isContains(Vector2 point) {
        float dx = pos.x - point.x;
        float dy = pos.y - point.y;
        return dx * dx + dy * dy <= R * R;
    }

    public boolean Overlaps(Circle c) {
        float dx = pos.x - c.pos.x;
        float dy = pos.y - c.pos.y;

        float dist = dx * dx + dy * dy;
        float sumR = c.R + R;
        return dist < sumR * sumR;
    }
}
