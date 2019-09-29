package com.timgapps.tds_game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.utils.rays.RayConfigurationBase;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.boontaran.games.StageGame;
import com.timgapps.tds_game.Setting;
import com.timgapps.tds_game.Tds;
import com.timgapps.tds_game.steer.Box2dSteeringEntity;
import com.timgapps.tds_game.steer.Box2dSteeringEntityOld;

public class Scene extends StageGame {
    protected ShapeRenderer shapeRenderer;
    private Box2DDebugRenderer debugRenderer;
    protected World world;
    private boolean drawDebug;
    private boolean hasBeenBuilt = false;

    RayConfigurationBase<Vector2>[] rayConfigurations;
    RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidanceSB;
    int rayConfigurationIndex;
    public Box2dSteeringEntityOld target;

    public Box2dSteeringEntityOld character;
    public Wander<Vector2> wanderSB;
    public BlendedSteering advancedSteering;
    private Body[] walls;
    private int[] walls_hw;
    private int[] walls_hh;

    public Scene() {
    }

    @Override
    protected void update(float delta) {
        super.update(delta);
    }

    private void build() {

        Gdx.input.setInputProcessor(stage);

        hasBeenBuilt = true;

        world = new World(new Vector2(0, -Setting.GRAVITY), true);
        debugRenderer = new Box2DDebugRenderer();
        drawDebug = true;

        createRandomWalls(2);
    }

    public void addChild(Actor actor) { // метод который будет принимать актера и выполнять добавление актера на 2D сцену
        this.stage.addActor(actor);
    }

    public static float pixelsToMeters(int pixels) {
        return (float) (pixels / Level.WORLD_SCALE);
    }

    /**
     * Методы для создания обхектов рулевого управления
     **/
    public Box2dSteeringEntity createSteeringEntity(World world, TextureRegion regionHull, TextureRegion regionTurret,
                                                    boolean independentFacing) {
        return createSteeringEntity(world, regionHull, regionTurret, independentFacing, Tds.V_WIDTH / 2, Tds.V_HEIGHT / 2);
    }

    public Box2dSteeringEntity createSteeringEntity(World world, TextureRegion regionHull, TextureRegion regionTurret) {
        return createSteeringEntity(world, regionHull, regionTurret, false);
    }

    public Box2dSteeringEntity createSteeringEntity(World world, TextureRegion regionHull, TextureRegion regionTurret,
                                                    boolean independentFacing, int posX, int posY) {

        int radiusInPixels = (int) ((regionHull.getRegionWidth() + regionHull.getRegionHeight()) / 4f);


        return new Box2dSteeringEntity(level, new Vector2(posX, posY), regionHull, regionTurret, independentFacing,
                pixelsToMeters(radiusInPixels));
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        build();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (Setting.DEBUG_WORLD) {
            if (hasBeenBuilt) {
                debugRenderer.render(world, camera.combined.cpy().scl(Level.WORLD_SCALE));
            }
        }

        GdxAI.getTimepiece().update(delta);
        float deltaTime = GdxAI.getTimepiece().getDeltaTime();
        character.update(deltaTime);
        world.step(deltaTime, 8, 3);

        if (drawDebug) {
            // Draw circle
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 1);
            float wanderCenterX = Level.WORLD_SCALE * (wanderSB.getWanderCenter().x);
            float wanderCenterY = Level.WORLD_SCALE * (wanderSB.getWanderCenter().y);
            float wanderRadius = Level.WORLD_SCALE * (wanderSB.getWanderRadius());
            shapeRenderer.circle(wanderCenterX, wanderCenterY, wanderRadius);
            shapeRenderer.end();

            // Draw target
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1);
            float targetCenterX = Level.WORLD_SCALE * (wanderSB.getInternalTargetPosition().x);
            float targetCenterY = Level.WORLD_SCALE * (wanderSB.getInternalTargetPosition().y);
            shapeRenderer.circle(targetCenterX, targetCenterY, 4);
            shapeRenderer.end();
        }
    }

    private void createRandomWalls(int n) {
        PolygonShape groundPoly = new PolygonShape();

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = groundPoly;
//        fixtureDef.filter.groupIndex = 0;

        walls = new Body[n];
        walls_hw = new int[n];
        walls_hh = new int[n];
        for (int i = 0; i < n; i++) {
            groundBodyDef.position.set((MathUtils.random(50, Tds.V_WIDTH - 50) / Level.WORLD_SCALE),
                    (MathUtils.random(50, Tds.V_HEIGHT - 50)) / Level.WORLD_SCALE);
            walls[i] = world.createBody(groundBodyDef);
            walls_hw[i] = (int) MathUtils.randomTriangular(20, 150);
            walls_hh[i] = (int) MathUtils.randomTriangular(30, 80);
            groundPoly.setAsBox((walls_hw[i]) / Level.WORLD_SCALE, (walls_hh[i]) / Level.WORLD_SCALE);
            walls[i].createFixture(fixtureDef);

        }
        groundPoly.dispose();
    }
}
