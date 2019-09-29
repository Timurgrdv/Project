package com.timgapps.tds_game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.timgapps.tds_game.Tds;
import com.timgapps.tds_game.levels.Level;
import com.timgapps.tds_game.steer.Box2dLocation;
import com.timgapps.tds_game.steer.Box2dSteeringUtils;

import java.util.ArrayList;

public class Enemy extends Tank implements Steerable<Vector2> {
    private Actor turret, hull;
    private Level level;
    public Body body;

    float boundingRadius;
    boolean tagged;
    private float maxLinearSpeed;
    private float maxLinearAcceleration;
    private float maxAngularSpeed;
    private float maxAngularAcceleration;

    private boolean indepenfantFacing;
    private SteeringBehavior<Vector2> steeringBehavior;
    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());


    private Vector2 linearVelocity;
    private float angularVelocity;
    float maxSpeed;
    private boolean independentFacing;
    private float vel = 0;
    private float currentAngle = 0;
    private boolean specialCase = false;
    public boolean rotateLeft, rotateRight;
    private float angle = 0;
    public boolean isRotate = false;

    private ArrayList<Enemy> arrayEnemy;

    public Enemy(Level level, Vector2 position, float rotation, TextureRegion textureHull, TextureRegion textureTurret,
                 float boundingRadius) {
        super(level, position, rotation, textureHull, textureTurret, "enemy", 10);

        this.level = level;
        body = getFBbody();
        body.setUserData(this);
        this.boundingRadius = boundingRadius;
        this.tagged = false;

        hull = getHull();
        turret = getTurret();
        linearVelocity = new Vector2(0, 0);
    }

    public boolean isIndependentFacing() {
        return independentFacing;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    /**
     * Здесь вы должны реализовать отсутствующие методы, унаследованные от Steerable * /
     */

    // Фактическая реализация зависит от вашей системы координат.
    // Здесь мы предполагаем, что ось Y направлена вверх.
    @Override
    public float vectorToAngle(Vector2 vector) {
        return Box2dSteeringUtils.vectorToAngle(vector);
    }

    // Фактическая реализация зависит от вашей системы координат.
    // Здесь мы предполагаем, что ось Y направлена вверх.
    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Box2dSteeringUtils.angleToVector(outVector, angle);
    }

    public SteeringBehavior<Vector2> getSteeringBehavior () {
        return steeringBehavior;
    }

    public void update (float deltaTime) {
        if (steeringBehavior != null) {
            // Calculate steering acceleration
            steeringBehavior.calculateSteering(steeringOutput);

            /*
             * Here you might want to add a motor control layer filtering steering accelerations.
             *
             * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
             * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
             * accelerate; and it only moves in the direction it is facing (ignoring power slides).
             */

            // Apply steering acceleration
            applySteering(steeringOutput, deltaTime);
        }


        wrapAround((1 / Level.WORLD_SCALE * Tds.V_WIDTH),
                (1 / Level.WORLD_SCALE * Tds.V_HEIGHT));

//        wrapAround(Box2dSteeringTest.pixelsToMeters(Gdx.graphics.getWidth()),
//                Box2dSteeringTest.pixelsToMeters(Gdx.graphics.getHeight()));
    }

    protected void applySteering (SteeringAcceleration<Vector2> steering, float deltaTime) {
        boolean anyAccelerations = false;

        // Update position and linear velocity.
        if (!steeringOutput.linear.isZero()) {
            // this method internally scales the force by deltaTime
            body.applyForceToCenter(steeringOutput.linear, true);
            anyAccelerations = true;
        }

        // Update orientation and angular velocity
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {
                // this method internally scales the torque by deltaTime
                body.applyTorque(steeringOutput.angular, true);
                anyAccelerations = true;
            }
        } else {
            // If we haven't got any velocity, then we can do nothing.
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
                body.setTransform(body.getPosition(), newOrientation);
            }
        }

        if (anyAccelerations) {
            // body.activate();

            // TODO:
            // Looks like truncating speeds here after applying forces doesn't work as expected.
            // We should likely cap speeds form inside an InternalTickCallback, see
            // http://www.bulletphysics.org/mediawiki-1.5.8/index.php/Simulation_Tick_Callbacks

            // Cap the linear speed
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            float maxLinearSpeed = getMaxLinearSpeed();
            if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
            }

            // Cap the angular speed
            float maxAngVelocity = getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngVelocity) {
                body.setAngularVelocity(maxAngVelocity);
            }
        }
    }

    // the display area is considered to wrap around from top to bottom
    // and from left to right
    protected void wrapAround (float maxX, float maxY) {
        float k = Float.POSITIVE_INFINITY;
        Vector2 pos = body.getPosition();

        if (pos.x > maxX) k = pos.x = 0.0f;

        if (pos.x < 0) k = pos.x = maxX;

        if (pos.y < 0) k = pos.y = maxY;

        if (pos.y > maxY) k = pos.y = 0.0f;

        if (k != Float.POSITIVE_INFINITY) body.setTransform(pos, body.getAngle());
    }

    public void setSteeringBehavior (SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }


    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    @Override
    public float getAngularVelocity() {
        return body.getAngularVelocity();
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new Box2dLocation();
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return 0.001f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }


    @Override
    public void setOrientation(float orientation)  {
        body.setTransform(getPosition(), orientation);
    }




//    @Override
//    public void act(float delta) {
//        super.act(delta);
//
//        if (steeringBehavior != null) {
//            //Рассчитаем ускорение рулевого управления
//            steeringBehavior.calculateSteering(steeringOutput);
//
//            /**
//             * Здесь вы можете добавить слой управления двигателем, фильтрующий ускорения рулевого управления.
//             *
//             * Например, автомобиль в игре вождения имеет физические ограничения на движение:
//             * - не может поворачиваться в неподвижном состоянии
//             * - чем быстрее он движется, тем медленнее он может поворачиваться (не входя в занос)
//             * - он может тормозить намного быстрее, чем ускоряться
//             * - он движется только в том направлении, в котором он стоит (игнорируя пачки мощности)
//             **/
//
//            /** Применим ускорение рулевого управления для перемещения этого агента */
//            applySteering(steeringOutput, delta);
//        }
//
////        // поворачиваем корпус танка
////        rotateHull(rotateLeft, rotateRight);
//////        setVelocity(new Vector2(0, 0));
//    }

    // задаём угол, на который поворачиваем башню танка
    public void setTurretRotation(Vector2 dir) {
        double cos = dir.x / Math.sqrt((dir.x * dir.x + dir.y * dir.y));

        // запоминаем начальный угол
        float angleTur = (float) Math.toDegrees(Math.acos(cos));
        if (dir.y < 0 && dir.x < 0) angleTur = (180 - angleTur) + 180;
        if (dir.y <= 0 && dir.x >= 0) angleTur = 360 - angleTur;
        if (angleTur >= 360) angleTur -= 360;
        if (angleTur < 0) angleTur += 360;

        turret.setRotation(angleTur);
    }

}
