package com.timgapps.tds_game.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.timgapps.tds_game.Tds;
import com.timgapps.tds_game.levels.Level;

import org.w3c.dom.css.Rect;

public class Bullet extends DynamicObject implements IBody {
    //public class Bullet extends Actor {
    private float posX, posY;
    private TextureRegion image;
    private Vector2 velocity;
    private Vector2 position;
    private Vector2 target;
    private Level level;
    private float speed = 10;
    private float rotation;
    private float MAX_DISTANCE = 200;
    private Rectangle bounds;
    private int objectType;

    private World world;
    private Body body;
    private float damage;


    public Bullet(Level level, Vector2 position, float rotation, int objectType, TextureRegion textureRegion, float damage) {
        super(level, position, rotation, objectType, textureRegion);
        this.level = level;
        this.position = position;
        this.world = level.getWorld();
        body = createBody(world, "bullet");
        image = textureRegion;
        this.rotation = rotation;
        this.objectType = objectType;
        this.damage = damage;

        velocity = new Vector2(5, 0);
        velocity.setAngle(rotation);
//        velocity.x = (float) (speed * Math.cos(Math.toRadians(rotation)));
//        velocity.y = (float) (speed * Math.sin(Math.toRadians(rotation)));

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
//        batch.draw(image, getX(), getY() - image.getRegionHeight() / 2, image.getRegionWidth() / 2, image.getRegionHeight() / 2,
//                image.getRegionWidth(), image.getRegionHeight(), 1, 1, rotation);

        batch.draw(image, getX(), getY(), 0, image.getRegionHeight() / 2,
                image.getRegionWidth(), image.getRegionHeight(), 1, 1, rotation);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        body.setLinearVelocity(velocity);
        setX(body.getPosition().x  * Level.WORLD_SCALE - 4);
        setY(body.getPosition().y * Level.WORLD_SCALE - 4);


//        position.add(velocity);
//        setPosition(position.x, position.y);


    }

    // метод для проверки столкновения с объектами
    public boolean checkCollision(Rectangle rectangle) {
        return rectangle.overlaps(bounds);
    }

    @Override
    public Body createBody(World world, String name) {
        CircleShape circleChape = new CircleShape();
        circleChape.setPosition(new Vector2());
        float radiusInPixels = 4 / Level.WORLD_SCALE;
//        int radiusInPixels = (int) ((region.getRegionWidth() + region.getRegionHeight()) / 4f);
        circleChape.setRadius(radiusInPixels);
//
        BodyDef characterBodyDef = new BodyDef();
        characterBodyDef.position.set(position.x / Level.WORLD_SCALE, position.y / Level.WORLD_SCALE);
        characterBodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(characterBodyDef);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = circleChape;
        fDef.filter.categoryBits = Tds.BULLET_BIT;
        fDef.filter.maskBits = Tds.ENEMY_BIT;
        fDef.density = 0.01f;
//        fixtureDef.filter.groupIndex = 0;
        body.createFixture(fDef).setUserData(this);

        circleChape.dispose();

        return body;
    }


    public float getDamage() {
        return damage;
    }
}
