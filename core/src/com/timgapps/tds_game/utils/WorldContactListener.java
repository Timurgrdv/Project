package com.timgapps.tds_game.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.timgapps.tds_game.Tds;
import com.timgapps.tds_game.entities.Bullet;
import com.timgapps.tds_game.entities.Player;
import com.timgapps.tds_game.entities.Tank;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case Tds.BULLET_BIT | Tds.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == Tds.ENEMY_BIT) {
                    System.out.println("tada");
                    ((Tank) fixA.getUserData()).hit(((Bullet) fixB.getUserData()).getDamage());
                } else {
                    ((Tank) fixB.getUserData()).hit(((Bullet) fixA.getUserData()).getDamage());
                    System.out.println("TADA!");
                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
