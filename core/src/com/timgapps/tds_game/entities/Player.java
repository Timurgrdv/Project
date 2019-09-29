package com.timgapps.tds_game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.timgapps.tds_game.Tds;
import com.timgapps.tds_game.controls.Joystick;
import com.timgapps.tds_game.levels.Level;

public class Player extends Tank {
    private Actor turret, hull;
    private float rotation;
    private float currentAngle = 0;
    private Level level;
    private Vector2 velocity, position, direction;
    private Joystick joystick;
    public boolean isMove = false;
    public boolean isRotate = false;

    private boolean specialCase = false;

    public static final int LEFT_ROTATE = 1;
    public static final int RIGHT_ROTATE = 2;
    private static final float VELOCITY = 1;
    public int rotationDir;

    public boolean rotateLeft, rotateRight;

    private float angle = 0;

    private float maxVelocity = 3, w, h;
    private float acceleration = 0.03f;
    private float vel = 0;
    private float attackDistance = 100;
    float deltaTime = 60;
    float R;

    public static float HULL_WIDTH;
    public static float HULL_HEIGHT;

    public static float TURRET_WIDTH;
    public static float TURRET_HEIGHT;
    private static float damage = 20;

    public Player(Level level, Vector2 position, float rotation, TextureRegion textureHull, TextureRegion textureTurret) {
        super(level, position, rotation, textureHull, textureTurret, "player", damage);
        this.level = level;

        hull = getHull();
        turret = getTurret();

        HULL_WIDTH = textureHull.getRegionWidth();
        // можно закоментрировать //
        HULL_HEIGHT = textureHull.getRegionHeight();
        TURRET_WIDTH = textureTurret.getRegionWidth();
        TURRET_HEIGHT = textureTurret.getRegionHeight();
        R = HULL_WIDTH;

        rotateLeft = false;
        rotateRight = false;

        // направление движения
        direction = new Vector2(0, 0);

        // скорость движения
        velocity = new Vector2(0, 0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        float angle = getFBbody().getAngle();
        currentAngle = (float) Math.toDegrees(angle);

        rotateHull(rotateLeft, rotateRight);
        updatePosition();
    }

    // Метод для вращения корпуса танка
    public void rotateHull(boolean rotateLeft, boolean rotateRight) {
        this.rotateLeft = rotateLeft;
        this.rotateRight = rotateRight;
        if (rotateLeft) {

            getFBbody().setAngularVelocity(2f);
//            currentAngle += 2;
//            setTankRotation(currentAngle);

            if (currentAngle > angle && !specialCase) {
                isRotate = false;
                this.rotateLeft = false;
            }
            if (currentAngle >= 360) {
                System.out.println("curAngle = " + currentAngle);
                currentAngle -= 360;
                System.out.println("new CurAngle + 360 = " + currentAngle);
                specialCase = false;
            }
        } else if (rotateRight) {
            getFBbody().setAngularVelocity(-2f);
//            currentAngle -= 2;
//            setTankRotation(currentAngle);

            if (currentAngle < angle && !specialCase) {
                isRotate = false;
                this.rotateRight = false;
            }
            if (currentAngle < 0) {
                System.out.println("curAngle = " + currentAngle);
                currentAngle += 360;
                System.out.println("new CurAngle + 360 = " + currentAngle);
                specialCase = false;
            }
        }
    }

    // метод для вращения башни танка


    public void updatePosition() {
        if (isMove) {
            if (vel < VELOCITY) {
                vel+= 0.02f;
            }
            move(new Vector2(vel, 0).setAngle(currentAngle));
        } else {
            move(new Vector2(0, 0));
            vel = 0;
        }
    }

    // метод для определения направления вращения
    public void setRotationDir(float angle1, float angle2) {
        int q1, q2;
        q1 = getQuadrant(angle1);
        q2 = getQuadrant(angle2);

        if (q1 == q2) {
            specialCase = false;
            if (angle1 < angle2) rotateLeft = true;
            else rotateRight = true;
        }

        if (q1 == 1 && q2 == 2) {
            rotateLeft = true;
        }
        if (q1 == 1 && q2 == 3) {
            if (angle2 < angle1 + 180) {
                rotateLeft = true;
            } else {
                rotateRight = true;
                specialCase = true;
            }
        }

        if (q1 == 2 && q2 == 3) {
            rotateLeft = true;
        }

        if (q1 == 2 && q2 == 1) {
            rotateRight = true;
        }

        if (q1 == 3 && q2 == 2) {
            rotateRight = true;
        }
        if (q1 == 3 && q2 == 4) {
            rotateLeft = true;
        }

        if (q1 == 4 && q2 == 3) {
            rotateRight = true;
        }

        if (q1 == 4 && q2 == 2) {
            if (angle1 - angle > 180) {
                rotateLeft = true;
            } else rotateRight = true;
        }

        specialCase = false;

        if (q1 == 3 && q2 == 1) {
            if (angle2 > angle1 - 180) {
                rotateRight = true;
            } else {
                rotateLeft = true;
                specialCase = true;
            }
        }

        if (q1 == 2 && q2 == 4) {
            if (angle2 < angle1 + 180) {
                rotateLeft = true;
            } else {
                rotateRight = true;
                specialCase = true;
            }
        }

        if (q1 == 4 && q2 == 1) {
            rotateLeft = true;
            specialCase = true;
        }

        if (q1 == 1 && q2 == 4) {
            rotateRight = true;
            specialCase = true;
        }
    }

    // вычисляем угол, на который будем поворачивать корпус танка
    public void setHullRotation(Vector2 dir) {
        double cos = dir.x / Math.sqrt((dir.x * dir.x + dir.y * dir.y));

        // запоминаем начальный угол
        angle = (float) Math.toDegrees(Math.acos(cos));
        if (dir.y < 0 && dir.x < 0) angle = (180 - angle) + 180;
        if (dir.y <= 0 && dir.x >= 0) angle = 360 - angle;
        setRotationDir(currentAngle, angle);
    }

    public int getQuadrant(float angle) {
        int quadrant = 1;
        if ((angle >= 0) && (angle < 90)) quadrant = 1;
        if ((angle >= 90) && (angle < 180)) quadrant = 2;
        if ((angle >= 180) && (angle < 270)) quadrant = 3;
        if ((angle >= 270) && (angle < 360)) quadrant = 4;
        return quadrant;
    }

    public void fire() {
        float R = TURRET_WIDTH;
        float x = (float) (R * Math.cos(Math.toRadians(turret.getRotation())));
        float y = (float) (R * Math.sin(Math.toRadians(turret.getRotation())));

        level.addChild
                (new Bullet(level,
                        new Vector2(x, y).add(getX() + HULL_WIDTH / 2, getY() + HULL_HEIGHT / 2),
                        turret.getRotation(), GameObject.PLAYER_BULLET,
                        new TextureRegion(Tds.atlas.findRegion("bullet")),
                        getDamage()));
    }

    public void stopRotation() {
        isRotate = false;
        rotateLeft = false;
        rotateRight = false;
    }

    public void setTurretRotation(Vector2 dir) {
        double cos = dir.x / Math.sqrt((dir.x * dir.x + dir.y * dir.y));

        // запоминаем начальный угол
        float angleTur = (float) Math.toDegrees(Math.acos(cos));
        if (dir.y < 0 && dir.x < 0) angleTur = (180 - angleTur) + 180;
        if (dir.y <= 0 && dir.x >= 0) angleTur = 360 - angleTur;
        if (angleTur >= 360) angleTur -= 360;
        if (angleTur < 0) angleTur += 360;

        turret.setRotation(angleTur - getRotation());
    }
}
