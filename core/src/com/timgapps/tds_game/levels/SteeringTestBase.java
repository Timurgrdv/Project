package com.timgapps.tds_game.levels;

import com.badlogic.gdx.ai.steer.Limiter;

public abstract class SteeringTestBase {
    protected Level level;

    public SteeringTestBase(Level level) {
        this.level = level;
    }

    protected void addMaxLinearAccelerationController(final Limiter limiter, float value) {
        limiter.setMaxLinearAcceleration(value);
    }

    protected void addMaxLinearSpeedController(final Limiter limiter, float value) {
        limiter.setMaxLinearSpeed(value);
    }

    protected void addMaxAngularAccelerationController(final Limiter limiter, float value) {
        limiter.setMaxAngularAcceleration(value);
    }

    protected void addMaxAngularSpeedController(final Limiter limiter, float value) {
        limiter.setMaxAngularSpeed(value);
    }


}
