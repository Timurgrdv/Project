package com.timgapps.tds_game.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import com.timgapps.tds_game.Tds;
import com.timgapps.tds_game.levels.Level;
import com.timgapps.tds_game.levels.Scene;

public class Tank extends Group implements IBody {
    Body FBbody;
    private Actor hull, turret;
    private Level scene;
    private World world;

    public Vector2 position;
    public float orientation;

    public float TURRET_WIDTH;
    public float TURRET_HEIGHT;

    public float HULL_WIDTH;
    public float HULL_HEIGHT;

    public static final int LIVE = 0;
    public static final int DEAD = 1;
    private float damage;


    public int state = LIVE;

    public Tank(Level level, Vector2 position, float orientation, TextureRegion textureHull,
                TextureRegion textureTurret, String name, float damage) {

        this.hull = new Hull(level, new Vector2(position.x, position.y), 0, GameObject.PLAYER, textureHull);
        this.turret = new Turret(level, new Vector2(position.x, position.y), 0, GameObject.PLAYER, textureTurret);
        this.scene = this.scene;
        this.world = this.scene.getWorld();
        this.orientation = orientation;
        this.position = position;
        this.damage = damage;

        addActor(hull);
        addActor(turret);

        TURRET_WIDTH = textureTurret.getRegionWidth();
        TURRET_HEIGHT = textureTurret.getRegionHeight();

        HULL_WIDTH = textureHull.getRegionWidth();
        // можно закоментрировать //
        HULL_HEIGHT = textureHull.getRegionHeight();

        createBody(world, name);
        setOrigin(HULL_WIDTH / 2, HULL_HEIGHT / 2);
        this.scene.addChild(this, position.x, position.y);
    }

    public Actor getHull() {
        return hull;
    }

    public Actor getTurret() {
        return turret;
    }

//    public Vector2 getPosition() {
//        return position;
//    }

    @Override
    public void act(float delta) {
        super.act(delta);
        hull.setX(getParent().getX());
        hull.setY(getParent().getY());
        turret.setX(getParent().getX() + HULL_WIDTH / 2);
        turret.setY(getParent().getY() + HULL_HEIGHT / 2 - TURRET_HEIGHT / 2);

        setPosition(FBbody.getPosition().x * Level.WORLD_SCALE - (HULL_WIDTH / 2),
                FBbody.getPosition().y * Level.WORLD_SCALE - HULL_HEIGHT / 2);
//        setRotation((float) Math.toDegrees(FBbody.getAngle()));
        hull.setRotation((float) Math.toDegrees(FBbody.getAngle()));
//        FBbody.setTransform(FBbody.getPosition().x, FBbody.getPosition().y, (float) (FBbody.getAngle() + Math.PI / 2));

//        hull.setX(getParent().getX());
//        hull.setY(getParent().getY());
    }

    @Override
    public float getWidth() {
        return hull.getWidth();
    }

    @Override
    public float getHeight() {
        return hull.getHeight();
    }

    @Override
    public Body createBody(World world, String name) {
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        FBbody = world.createBody(def);

        FixtureDef fDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(HULL_HEIGHT / 2 / Level.WORLD_SCALE, HULL_WIDTH / 2 / Level.WORLD_SCALE);
//        shape.setAsBox(HULL_WIDTH / 2 / Level.WORLD_SCALE, HULL_HEIGHT / 2 / Level.WORLD_SCALE);
        fDef.shape = shape;
        if (name.equals("enemy")) {
            fDef.filter.categoryBits = Tds.ENEMY_BIT;
            fDef.filter.maskBits = Tds.BULLET_BIT | Tds.PLAYER_BIT;
            System.out.println("enemy");
        } else if (name.equals("player")) {
            fDef.filter.categoryBits = Tds.PLAYER_BIT;
            fDef.filter.maskBits = Tds.BULLET_BIT | Tds.ENEMY_BIT;
        }
        fDef.density = 1;
        FBbody.createFixture(fDef).setUserData(this);
        FBbody.setLinearDamping(10);
        FBbody.setAngularDamping(50);
        shape.dispose();
        FBbody.setTransform((position.x + HULL_WIDTH / 2) / Level.WORLD_SCALE, position.y / Level.WORLD_SCALE, 0);
        return FBbody;
    }

    public void setTankRotation(float angle) {
        FBbody.setTransform(FBbody.getPosition().x, FBbody.getPosition().y, (float) Math.toRadians(angle));
    }

    public float getTankRotation() {
        return 0;
    }

    public void move(Vector2 velocity) {
        FBbody.setLinearVelocity(velocity);
    }

    public Body getFBbody() {
        return FBbody;
    }

    public void setVelocity(Vector2 velocity) {
        FBbody.setLinearVelocity(velocity.x, velocity.y);
    }

    public void setTurretOrientation(float orientation) {
        turret.setRotation(orientation);
//        turret.setRotation(getRotation() + orientation);
    }

    public void hit(float damage) {
        System.out.println("DAMAGEEEE!!!");
    }


    public float getDamage() {
        return damage;
    }


}