package com.timgapps.tds_game.steer;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.timgapps.tds_game.Tds;
import com.timgapps.tds_game.entities.Tank;
import com.timgapps.tds_game.levels.Level;

public class Box2dSteeringEntityOld extends Tank implements Steerable<Vector2> {
    //public class Box2dSteeringEntityOld extends Actor implements Steerable<Vector2> {
    TextureRegion textureHull;
    public Body body;

    public float boundingRadius;
    public boolean tagged;

    float maxLinearSpeed;
    float maxLinearAcceleration;

    public void update(float deltaTime) {
        if (steeringBehavior != null) {
            // Calculate steering acceleration
            steeringBehavior.calculateSteering(steeringOutput);
            System.out.println("steering.angular = " + steeringOutput.angular);

            /** Здесь вы можете добавить слой управления двигателем, фильтрующий ускорения рулевого управления.
                           *
                           * Например, автомобиль в игре с вождением имеет физические ограничения на движение: он не может вращаться, пока он находится в неподвижном состоянии;
                           * чем быстрее он движется, тем медленнее он может поворачиваться (не входя в занос); он может тормозить гораздо быстрее, чем может
                           * ускоряться; и он движется только в том направлении, в котором он стоит (игнорируя скольжения мощности).
                           * /

             */
            // Apply steering acceleration

            applySteering(steeringOutput, deltaTime);
        }
        wrapAround(Level.pixelsToMeters(Tds.V_WIDTH), Level.pixelsToMeters(Tds.V_HEIGHT));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
//        if (steeringBehavior != null) {
//            // Calculate steering acceleration
//            steeringBehavior.calculateSteering(steeringOutput);
//            System.out.println("steering.angular = " + steeringOutput.angular);
//
//            /** Здесь вы можете добавить слой управления двигателем, фильтрующий ускорения рулевого управления.
//                           *
//                           * Например, автомобиль в игре с вождением имеет физические ограничения на движение: он не может вращаться, пока он находится в неподвижном состоянии;
//                           * чем быстрее он движется, тем медленнее он может поворачиваться (не входя в занос); он может тормозить гораздо быстрее, чем может
//                           * ускоряться; и он движется только в том направлении, в котором он стоит (игнорируя скольжения мощности).
//                           * /
//
//             */
//            // Apply steering acceleration
//
//            applySteering(steeringOutput, delta);
//        }
//        wrapAround(Level.pixelsToMeters(Tds.V_WIDTH), Level.pixelsToMeters(Tds.V_HEIGHT));

    }

    float maxAngularSpeed;
    float maxAngularAcceleration;

    boolean independentFacing;

    // переменная рулевого управления
    public SteeringBehavior<Vector2> steeringBehavior;

    // вычиисленное значение ускорений
    public SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());

    public Box2dSteeringEntityOld(Level level, Vector2 position, TextureRegion textureHull,
                                  TextureRegion textureTurret, boolean independentFacing, float boundingRadius) {
        super(level, position, 0, textureHull, textureTurret, "enemy", 10);
        this.body = getFBbody();
        this.independentFacing = independentFacing;
        this.boundingRadius = boundingRadius;
        this.tagged = false;

        body.setUserData(this);

    }

    public TextureRegion getTexturHull() {
        return textureHull;
    }

    public void setTexturHull(TextureRegion texturHull) {
        this.textureHull = texturHull;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public boolean isIndependentFacing() {
        return independentFacing;
    }

    public void setIndependentFacing(boolean independentFacing) {
        this.independentFacing = independentFacing;
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }

    @Override
    public float getOrientation() {
        return body.getAngle();
    }

    @Override
    public void setOrientation(float orientation) {
        body.setTransform(getPosition(), orientation);
    }

    @Override
    public Vector2 getLinearVelocity() {
        return body.getLinearVelocity();
    }

    /**
     * Возвращает значение с плавающей запятой, указывающее угловую скорость в радианах этого Steerable.
     */
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
    public float vectorToAngle(Vector2 vector) {
        return Box2dSteeringUtils.vectorToAngle(vector);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        return Box2dSteeringUtils.angleToVector(outVector, angle);
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
        return steeringBehavior;
    }

    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
        this.steeringBehavior = steeringBehavior;
    }

//    public void update(float deltaTime) {
//        if (steeringBehavior != null) {
//            // Calculate steering acceleration
//            steeringBehavior.calculateSteering(steeringOutput);
//
//            /** Здесь вы можете добавить слой управления двигателем, фильтрующий ускорения рулевого управления.
//                           *
//                           * Например, автомобиль в игре с вождением имеет физические ограничения на движение: он не может вращаться, пока он находится в неподвижном состоянии;
//                           * чем быстрее он движется, тем медленнее он может поворачиваться (не входя в занос); он может тормозить гораздо быстрее, чем может
//                           * ускоряться; и он движется только в том направлении, в котором он стоит (игнорируя скольжения мощности).
//                           * /
//
//             */
//            // Apply steering acceleration
//
//            applySteering(steeringOutput, deltaTime);
//        }
//
//        wrapAround(Tds.V_WIDTH / Level.WORLD_SCALE,
//                Tds.V_HEIGHT / Level.WORLD_SCALE);
//    }

    /**
     * метод применяет рассчитанные ускорения и скорости к телу
     *
     * @param steering
     * @param deltaTime
     */
    protected void applySteering(SteeringAcceleration<Vector2> steering, float deltaTime) {
        boolean anyAccelerations = false;

        // Update position and linear velocity.
        if (!steeringOutput.linear.isZero()) {
            //этот метод внутренне масштабирует силу с помощью deltaTime
            body.applyForceToCenter(steeringOutput.linear, true);
            anyAccelerations = true;
        }

        // Обновление ориентации и угловой скорости
        if (isIndependentFacing()) {
            if (steeringOutput.angular != 0) {
                // this method internally scales the torque by deltaTime
                body.applyTorque(steeringOutput.angular, true);
                anyAccelerations = true;
            }
        } else {
            // Если у нас нет скорости, то мы ничего не можем сделать.
            Vector2 linVel = getLinearVelocity();
            if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
                float newOrientation = vectorToAngle(linVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
                body.setTransform(body.getPosition(), newOrientation);
//                body.applyTorque(steeringOutput.angular, true);

//                 повернем на 90 градусов, а то глюк
//                body.setTransform(body.getPosition(), (float) (newOrientation + Math.PI / 2));
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
                body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float) Math.sqrt(currentSpeedSquare)));
            }

            // Cap the angular speed
            float maxAngVelocity = getMaxAngularSpeed();
            if (body.getAngularVelocity() > maxAngVelocity) {
                body.setAngularVelocity(maxAngVelocity);
            }
        }
    }

    // метод для разворота
    protected void wrapAround(float maxX, float maxY) {
        float k = Float.POSITIVE_INFINITY;
        Vector2 pos = body.getPosition();

        if (pos.x > maxX) k = pos.x = 0.0f;

        if (pos.x < 0) k = pos.x = maxX;

        if (pos.y < 0) k = pos.y = maxY;

        if (pos.y > maxY) k = pos.y = 0.0f;

        if (k != Float.POSITIVE_INFINITY) body.setTransform(pos, body.getAngle());
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
    public float getZeroLinearSpeedThreshold() {
        return 0.3f;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        throw new UnsupportedOperationException();
    }
}
