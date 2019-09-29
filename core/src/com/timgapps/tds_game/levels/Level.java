package com.timgapps.tds_game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.Limiter;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.Wander;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.ParallelSideRayConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.RayConfigurationBase;
import com.badlogic.gdx.ai.steer.utils.rays.SingleRayConfiguration;
import com.badlogic.gdx.ai.utils.RaycastCollisionDetector;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.boontaran.games.StageGame;

import com.timgapps.tds_game.Setting;
import com.timgapps.tds_game.Tds;
import com.timgapps.tds_game.controls.Joystick;
import com.timgapps.tds_game.entities.Bullet;
import com.timgapps.tds_game.entities.Enemy;
import com.timgapps.tds_game.entities.Player;
import com.timgapps.tds_game.entities.Tank;
import com.timgapps.tds_game.entities.Tree;
import com.timgapps.tds_game.steer.Box2dRaycastCollisionDetector;
import com.timgapps.tds_game.steer.Box2dSteeringEntityOld;
import com.timgapps.tds_game.utils.WorldContactListener;

public class Level extends StageGame {

    boolean drawDebug;
    ShapeRenderer shapeRenderer;
    private Batch spriteBatch;

    float waitMove = 60;


    public static final float WORLD_SCALE = 100;
    private boolean hasBeenBuilt = false;
    public static final float MIN_DISTANCE = 180;

    private float waitTarget = 100;


    protected World world;
    private String directory;
    private Player player;
    private Array<Enemy> arrayEnemy;

    private Bullet bullet;
    private Tree tree;

    private Tank tank;

    private TiledMap map;
    private int mapWidth, mapHeight, tilePixelWidth, tilePixelHeight, levelWidth, levelHeight;

    public static final int ON_RESTART = 1;
    public static final int ON_QUIT = 2;
    public static final int ON_COMPLETED = 3;
    public static final int ON_FAILED = 4;
    public static final int ON_PAUSED = 5;
    public static final int ON_RESUME = 6;

    // добавим несколько констант для хранения состояния игры
    public static final int PLAY = 1;
    public static final int LEVEL_FAILED = 2;
    public static final int LEVEL_COMPLETED = 3;
    public static final int PAUSED = 4;
    public static final int IS_TIME_UP = 5;
    private Joystick joyStick;

    private Touchpad leftTouchpad, rightTouchPad;

    private float touchpadWidth = 200;
    private float touchpadHeight = 200;
    private TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable touchBackground;
    private Drawable touchKnob;
    private ImageButton attackButton;

    private boolean isOk = false;

    int state;
    static final int GAME_READY = 0;
    static final int GAME_RUNNING = 1;
    static final int GAME_PAUSED = 2;
    static final int GAME_LEVEL_END = 3;
    static final int GAME_OVER = 4;

    private Box2DDebugRenderer debugRenderer;
    public Box2dSteeringEntityOld character;
    RayConfigurationBase<Vector2>[] rayConfigurations;
    RaycastObstacleAvoidance<Vector2> raycastObstacleAvoidanceSB;
    int rayConfigurationIndex;
    public Box2dSteeringEntityOld target;
    public Vector2 distance = new Vector2();
    public Wander<Vector2> wanderSB;
    private float waitAttack = 120;

    public BlendedSteering advancedSteering;

    private Body[] walls;
    private int[] walls_hw;
    private int[] walls_hh;

    private float attackDistance = 100;

    public Arrive<Vector2> arriveSB;

    public Level(String directory) {

        state = GAME_RUNNING;
        attackButton = new ImageButton(new TextureRegionDrawable(Tds.atlas.findRegion("attackBtn")),
                new TextureRegionDrawable(Tds.atlas.findRegion("attackBtn_down")));
    }

    @Override
    protected void update(float delta) {
        super.update(delta);


        float deltaTime = GdxAI.getTimepiece().getDeltaTime();
//        character.update(delta);
//        character.update(deltaTime);
//        world.step(deltaTime, 8, 3);


        if (leftTouchpad != null && player != null) {
//            // устанавливаем положение корпуса танка, в зависимости от угла повората

            if (leftTouchpad.isTouched() && (leftTouchpad.getKnobX() - touchpadWidth / 2) != 0 && (leftTouchpad.getKnobY() - touchpadHeight / 2) != 0) {
                float dx = leftTouchpad.getKnobX();
                float dy = leftTouchpad.getKnobY();
                player.setHullRotation(new Vector2(dx - 100, dy - 100));
            }

            // если нажат тачпад левый, задаем движение танку, isMove = true
            if (leftTouchpad.isTouched() && !player.isMove) {
                player.isMove = true;

            }
            if (!leftTouchpad.isTouched() && player.isMove) {
                player.isMove = false;
                player.stopRotation();
            }
        }

        if (rightTouchPad.isTouched() && rightTouchPad != null && ((rightTouchPad.getKnobX() - touchpadWidth / 2) != 0 && (rightTouchPad.getKnobY() - touchpadHeight / 2) != 0)) {
            float dx = rightTouchPad.getKnobX();
            float dy = rightTouchPad.getKnobY();
            player.setTurretRotation(new Vector2(dx - 100, dy - 100));
        }

        waitAttack--;
        Vector2 point1 = new Vector2(character.body.getPosition());
        Vector2 point2 = new Vector2(target.body.getPosition());
        distance = point1.sub(point2);

        if (Math.sqrt(distance.len2()) < attackDistance) {
            character.setTurretOrientation(distance.angle() + 180);
            if (waitAttack < 0) {
//                character.setTurretOrientation(distance.angle() + 180);
//                System.out.println("attack");
                waitAttack = 100;
            }
        }


//        updateWorld(delta);
//        character.update(delta);


//        target.setPosition(player.getFBbody().getPosition().x, player.getFBbody().getPosition().y, 0);
//        target.getBody().setTransform(player.getFBbody().getPosition().x, player.getFBbody().getPosition().y, 0);

    }

    public void updateWorld(float delta) {
        world.step(delta, 8, 3);
    }

    public World getWorld() {
        return world;
    }

    private void build() {

        world = new World(new Vector2(0, -Setting.GRAVITY), true);
        world.setContactListener(new WorldContactListener());

        hasBeenBuilt = true;
        debugRenderer = new Box2DDebugRenderer();

        //Create a leftTouchpad skin
        touchpadSkin = new Skin();

        //Set background image
        touchpadSkin.add("touchBackground", new TextureRegion(Tds.atlas.findRegion("joystick_bg")));
        //Set knob image
        touchpadSkin.add("touchKnob", new TextureRegion(Tds.atlas.findRegion("joystick_circle")));
        //Create TouchPad Style
        touchpadStyle = new TouchpadStyle();
        //Create Drawable's from TouchPad skin
        touchBackground = touchpadSkin.getDrawable("touchBackground");
        touchKnob = touchpadSkin.getDrawable("touchKnob");

        //Apply the Drawables to the TouchPad Style
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;
        //Create new TouchPad with the created style
        leftTouchpad = new Touchpad(10, touchpadStyle);
        //setBounds(x,y,width,height)
        leftTouchpad.setBounds(15, 15, touchpadWidth, touchpadHeight);

        rightTouchPad = new Touchpad(10, touchpadStyle);
        rightTouchPad.setBounds(Tds.V_WIDTH - 15 - 200, 15, touchpadWidth, touchpadHeight);

        Gdx.input.setInputProcessor(stage);

        addChild(leftTouchpad);
        addChild(rightTouchPad);

        attackButton.setPosition(800, 100);
        addChild(attackButton);
        attackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (player != null) player.fire();
            }
        });

        // создадим игрока
//        player = new Player(this, new Vector2(100, 500), 0, new TextureRegion(Tds.atlas.findRegion("tankBody_green_outline")),
//                new TextureRegion(Tds.atlas.findRegion("tankGreen_barrel3_outline")));


        player = new Player(this, new Vector2(100, 500), 0, new TextureRegion(Tds.atlas.findRegion("hullGreen")),
                new TextureRegion(Tds.atlas.findRegion("tankGreen_barrel3_outline")));


        drawDebug = true;
//        drawDebug = true;

        createRandomWalls(2);

        // create character
        character = createSteeringEntity(world, new TextureRegion(Tds.atlas.findRegion("hullGreen")),
//        character = createSteeringEntity(world, new TextureRegion(Tds.atlas.findRegion("tankBody_green_outline")),
                new TextureRegion(Tds.atlas.findRegion("tankGreen_barrel3_outline")), false);
        character.setMaxLinearAcceleration(11);
        character.setMaxLinearSpeed(2);
        character.setMaxAngularAcceleration(0.001f);
        character.setMaxAngularSpeed(0.02f);

        target = createSteeringEntity(world, new TextureRegion(Tds.atlas.findRegion("target")),
                new TextureRegion(Tds.atlas.findRegion("target")),
                false, (int) player.getX(), (int) player.getY());
        markAsSensor(target);
        Vector2 point1 = new Vector2(character.body.getPosition());
        Vector2 point2 = new Vector2(target.body.getPosition());
        distance = point1.sub(point2);

        @SuppressWarnings("unchecked")
        RayConfigurationBase<Vector2>[] localRayConfigurations = new RayConfigurationBase[]{
                new SingleRayConfiguration<Vector2>(character, pixelsToMeters(20)),
                new ParallelSideRayConfiguration<Vector2>(character, pixelsToMeters(100),
                        character.getBoundingRadius()),
                new CentralRayWithWhiskersConfiguration<Vector2>(character, pixelsToMeters(100),
                        pixelsToMeters(40), 35 * MathUtils.degreesToRadians)};
        rayConfigurations = localRayConfigurations;
        rayConfigurationIndex = 0;
        RaycastCollisionDetector<Vector2> raycastCollisionDetector = new Box2dRaycastCollisionDetector(world);
        raycastObstacleAvoidanceSB = new RaycastObstacleAvoidance<Vector2>(character, rayConfigurations[rayConfigurationIndex],
                raycastCollisionDetector, pixelsToMeters(1000));

        Limiter limiter = new Limiter() {
            @Override
            public float getZeroLinearSpeedThreshold() {
                return 0.1f;
            }

            @Override
            public void setZeroLinearSpeedThreshold(float value) {

            }

            @Override
            public float getMaxLinearSpeed() {
                return 1f;
            }

            @Override
            public void setMaxLinearSpeed(float maxLinearSpeed) {

            }

            @Override
            public float getMaxLinearAcceleration() {
                return 0.5f;
            }

            @Override
            public void setMaxLinearAcceleration(float maxLinearAcceleration) {

            }

            @Override
            public float getMaxAngularSpeed() {
                return 1f;
            }

            @Override
            public void setMaxAngularSpeed(float maxAngularSpeed) {

            }

            @Override
            public float getMaxAngularAcceleration() {
                return 0.005f;
            }

            @Override
            public void setMaxAngularAcceleration(float maxAngularAcceleration) {

            }
        };

        wanderSB = new Wander<Vector2>(character)
                .setFaceEnabled(false) // We want to use Face internally (independent facing is on)
                .setAlignTolerance(0.001f) // Used by Face
                .setDecelerationRadius(1) // Used by Face
                .setTimeToTarget(0.01f) // Used by Face
                .setWanderOffset(3) //  Устанавливает прямое смещение блуждания по кругу.
                .setWanderOrientation(3) // Устанавливает текущую ориентацию цели блуждания.
                .setWanderRadius(1)     //  Устанавливает радиус блуждающего круга.
                .setWanderRate(MathUtils.PI2 * 8); //6  // Устанавливает скорость, выраженную в радианах в секунду,

        // с которой может изменяться ориентация блуждания.
//        character.setSteeringBehavior(wanderSB);

        addMaxLinearAccelerationController(character, 0.4f);
        addMaxAngularAccelerationController(character, 6);
//        addMaxAngularAccelerationController(character, 2);
        addMaxAngularSpeedController(character, 10);
        addMaxLinearSpeedController(character, 1);

//        PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(character, 0.0001f) //
//                .add(raycastObstacleAvoidanceSB) //
//                .add(wanderSB);

        wanderSB.setLimiter(limiter);

        final Arrive<Vector2> arriveSB = new Arrive<Vector2>(character, target)
                .setTimeToTarget(0.5f) //       // Устанавливает время, за которое достигается заданная скорость.
                .setArrivalTolerance(2f)    // Устанавливает допуск для достижения цели.
//                .setArrivalTolerance(0.5f)    // Устанавливает допуск для достижения цели.
                // Это позволяет владельцу подойти достаточно близко к цели, не допуская мелких ошибок
                .setDecelerationRadius(2);     // Устанавливает радиус для замедления начала.


        advancedSteering = new BlendedSteering(character);
//        advancedSteering.add(arriveSB, 0.5f);   // 1
        advancedSteering.add(arriveSB, 0.3f);   // 0.3f
        advancedSteering.add(wanderSB, 0.9f);   // 0.8f
        advancedSteering.add(raycastObstacleAvoidanceSB, 0.4f);

//        character.setMaxLinearAcceleration(0.0003f);


        raycastObstacleAvoidanceSB.setLimiter(limiter);

        character.setSteeringBehavior(advancedSteering);

//        character.setSteeringBehavior(prioritySteeringSB);


//        addMaxAngularSpeedController(character, 0.02f);

//        wanderSB.setWanderOffset(2);
//        wanderSB.setWanderRadius(0.5f);
//        wanderSB.setWanderRate(25);

//        setParameters(character);
//        addMaxLinearSpeedController(character);

        // create target
//        target = createSteeringEntity(world, new TextureRegion(Tds.atlas.findRegion("target")),
//                new TextureRegion(Tds.atlas.findRegion("target")),
//                false, (int) player.getX(), (int) player.getY());
//        markAsSensor(target);
//        Vector2 point1 = new Vector2(character.body.getPosition());
//        Vector2 point2 = new Vector2(target.body.getPosition());
//        distance = point1.sub(point2);
    }

    public void addMaxLinearAccelerationController(final Limiter limiter, float value) {
        limiter.setMaxLinearAcceleration(value);
    }


    public void addMaxLinearSpeedController(final Limiter limiter, float value) {
        limiter.setMaxLinearSpeed(value);
    }

    public void addMaxAngularAccelerationController(final Limiter limiter, float value) {
        limiter.setMaxAngularAcceleration(value);
    }

    public void addMaxAngularSpeedController(final Limiter limiter, float value) {
        limiter.setMaxAngularSpeed(value);
    }

    protected void setParameters(final Limiter limiter) {
        limiter.setMaxLinearAcceleration(2);
//        limiter.setMaxLinearSpeed(3);
        limiter.setMaxAngularAcceleration(0.2f);
        limiter.setMaxAngularSpeed(0.8f);
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
            groundBodyDef.position.set((MathUtils.random(50, Tds.V_WIDTH - 50) / WORLD_SCALE),
                    (MathUtils.random(50, Tds.V_HEIGHT - 50)) / WORLD_SCALE);
            walls[i] = world.createBody(groundBodyDef);
            walls_hw[i] = (int) MathUtils.randomTriangular(20, 150);
            walls_hh[i] = (int) MathUtils.randomTriangular(30, 80);
            groundPoly.setAsBox((walls_hw[i]) / WORLD_SCALE, (walls_hh[i]) / WORLD_SCALE);
            walls[i].createFixture(fixtureDef);

        }
        groundPoly.dispose();
    }

    public void markAsSensor(Box2dSteeringEntityOld character) {
        Array<Fixture> fixtures = character.getBody().getFixtureList();
        for (int i = 0; i < fixtures.size; i++) {
            fixtures.get(i).setSensor(true);
        }
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        build();
    }


    @Override
    public void render(float delta) {
        super.render(delta);
        if (Setting.DEBUG_WORLD) {
            if (hasBeenBuilt) {
                debugRenderer.render(world, camera.combined.cpy().scl(WORLD_SCALE));
            }
        }


//        waitMove--;
//        if (waitMove < 0) {

        GdxAI.getTimepiece().update(delta);
        float deltaTime = GdxAI.getTimepiece().getDeltaTime();
        character.update(deltaTime);
        world.step(deltaTime, 8, 3);

//            waitMove = 10;
//        }

        if (drawDebug) {
            // Draw circle
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 1);
            float wanderCenterX = WORLD_SCALE * (wanderSB.getWanderCenter().x);
            float wanderCenterY = WORLD_SCALE * (wanderSB.getWanderCenter().y);
            float wanderRadius = WORLD_SCALE * (wanderSB.getWanderRadius());
            shapeRenderer.circle(wanderCenterX, wanderCenterY, wanderRadius);
            shapeRenderer.end();

            // Draw target
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1);
            float targetCenterX = WORLD_SCALE * (wanderSB.getInternalTargetPosition().x);
            float targetCenterY = WORLD_SCALE * (wanderSB.getInternalTargetPosition().y);
            shapeRenderer.circle(targetCenterX, targetCenterY, 4);
            shapeRenderer.end();
        }

    }

    public Enemy getEnemy(int i) {
        return arrayEnemy.get(i);
    }

    public Stage getStage() {
        return this.stage;
    }

    public Player getPlayer() {
        return player;
    }

    private void setBackGround(String region) {
        clearBackground();
        Image bg = new Image(Tds.atlas.findRegion(region));
        addBackground(bg, true, false);
    }

    public void addChild(Actor actor) { // метод который будет принимать актера и выполнять добавление актера на 2D сцену
        this.stage.addActor(actor);
    }

    public Tree getTree() {
        return tree;
    }

    public Array<Enemy> getArrayEnemy() {
        return arrayEnemy;
    }

    public static float pixelsToMeters(int pixels) {
        return (float) (pixels / WORLD_SCALE);
    }

    public Box2dSteeringEntityOld createSteeringEntity(World world, TextureRegion region,
                                                       TextureRegion regionTurret, boolean independentFacing) {
        return createSteeringEntity(world, region, regionTurret, independentFacing, Tds.V_WIDTH / 2, Tds.V_HEIGHT / 2);
    }


    public Box2dSteeringEntityOld createSteeringEntity(World world, TextureRegion region, TextureRegion regionTurret) {
        return createSteeringEntity(world, region, regionTurret, false);
    }

    public Box2dSteeringEntityOld createSteeringEntity(World world, TextureRegion region, TextureRegion regionTurret,
                                                       boolean independentFacing, int posX, int posY) {
//        CircleShape circleChape = new CircleShape();
//        circleChape.setPosition(new Vector2());
        int radiusInPixels = (int) ((region.getRegionWidth() + region.getRegionHeight()) / 4f);
//        circleChape.setRadius(pixelsToMeters(radiusInPixels));
//
//        BodyDef characterBodyDef = new BodyDef();
//        characterBodyDef.position.set(pixelsToMeters(posX), pixelsToMeters(posY));
//        characterBodyDef.type = BodyDef.BodyType.DynamicBody;
//        Body characterBody = world.createBody(characterBodyDef);
//
//        FixtureDef charFixtureDef = new FixtureDef();
//        charFixtureDef.shape = circleChape;
//        charFixtureDef.filter.groupIndex = 0;
//        characterBody.createFixture(charFixtureDef);

//        circleChape.dispose();


        return new Box2dSteeringEntityOld(this, new Vector2(posX, posY), region, regionTurret, independentFacing,
                pixelsToMeters(radiusInPixels));
//                region, characterBody, independentFacing, pixelsToMeters(radiusInPixels));
//        return new Box2dSteeringEntityOld(region, characterBody, independentFacing, pixelsToMeters(radiusInPixels));

//        new Player(this, new Vector2(100, 500), 0, new TextureRegion(Tds.atlas.findRegion("tankBody_green_outline")),
//                new TextureRegion(Tds.atlas.findRegion("tankGreen_barrel3_outline")));
    }


}




