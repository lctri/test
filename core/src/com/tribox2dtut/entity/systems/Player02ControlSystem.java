package com.tribox2dtut.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.tribox2dtut.LevelFactory;
import com.tribox2dtut.controller.KeyboardController;
import com.tribox2dtut.entity.components.B2dBodyComponent;
import com.tribox2dtut.entity.components.Player02Component;
import com.tribox2dtut.entity.components.StateComponent;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class Player02ControlSystem extends IteratingSystem {

    private LevelFactory lvlFactory;
    ComponentMapper<Player02Component> pm;
    ComponentMapper<B2dBodyComponent> bodm;
    ComponentMapper<StateComponent> sm;
    KeyboardController controller;


    @SuppressWarnings("unchecked")
    public Player02ControlSystem(KeyboardController keyCon, LevelFactory lvlf) {
        super(Family.all(Player02Component.class).get());
        controller = keyCon;
        lvlFactory = lvlf;
        pm = ComponentMapper.getFor(Player02Component.class);
        bodm = ComponentMapper.getFor(B2dBodyComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        B2dBodyComponent b2body = bodm.get(entity);
        StateComponent state = sm.get(entity);
        Player02Component player = pm.get(entity);

        System.out.println(state.get());
        player.cam.position.y = b2body.body.getPosition().y;


        if(b2body.body.getLinearVelocity().y > 0 && state.get() != StateComponent.STATE_FALLING){
            state.set(StateComponent.STATE_FALLING);
        }

        if(b2body.body.getLinearVelocity().y == 0){
            if(state.get() == StateComponent.STATE_FALLING){
                state.set(StateComponent.STATE_NORMAL);
            }
            if(b2body.body.getLinearVelocity().x != 0 && state.get() != StateComponent.STATE_MOVING){
                state.set(StateComponent.STATE_MOVING);
            }
        }

        if(b2body.body.getLinearVelocity().y < 0 && state.get() == StateComponent.STATE_FALLING){
            // player is actually falling. check if they are on platform
            if(player.onPlatform){
                //overwrite old y value with 0 t stop falling but keep x vel
                b2body.body.setLinearVelocity(b2body.body.getLinearVelocity().x, 0f);
            }
        }

        // make player teleport higher
        if(player.onSpring){
            //b2body.body.applyLinearImpulse(0, 175f, b2body.body.getWorldCenter().x,b2body.body.getWorldCenter().y, true);
            b2body.body.setTransform(b2body.body.getPosition().x, b2body.body.getPosition().y+ 10, b2body.body.getAngle());
            //state.set(StateComponent.STATE_JUMPING);
            player.onSpring = false;
        }


        if(controller.left){
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, -7f, 0.2f),b2body.body.getLinearVelocity().y);
        }
        if(controller.right){
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, 7f, 0.2f),b2body.body.getLinearVelocity().y);
        }

        if(!controller.left && ! controller.right){
            b2body.body.setLinearVelocity(MathUtils.lerp(b2body.body.getLinearVelocity().x, 0, 0.1f),b2body.body.getLinearVelocity().y);
        }

        if(controller.up &&
                (state.get() == StateComponent.STATE_NORMAL || state.get() == StateComponent.STATE_MOVING)){
            b2body.body.applyLinearImpulse(0, 12f * b2body.body.getMass() , b2body.body.getWorldCenter().x,b2body.body.getWorldCenter().y, true);
            state.set(StateComponent.STATE_JUMPING);
            player.onPlatform = false;
            player.onSpring = false;
        }

        if(controller.down){
            b2body.body.applyLinearImpulse(0, -5f, b2body.body.getWorldCenter().x,b2body.body.getWorldCenter().y, true);
        }

        if(player.timeSinceLastShot > 0){
            player.timeSinceLastShot -= deltaTime;
        }

        if(controller.isMouse1Down || true ){ // if mouse button is pressed
            //System.out.println(player.timeSinceLastShot+" ls:sd "+player.shootDelay);
            // user wants to fire
            if(player.timeSinceLastShot <=0){ // check the player hasn't just shot
                //player can shoot so do player shoot
                Vector3 mousePos = new Vector3(controller.mouseLocation.x,controller.mouseLocation.y,0); // get mouse position
                player.cam.unproject(mousePos); // convert position from screen to box2d world position
                float speed = 10f;  // set the speed of the bullet
                float shooterX = b2body.body.getPosition().x; // get player location
                float shooterY = b2body.body.getPosition().y; // get player location
                float velx = mousePos.x - shooterX; // get distance from shooter to target on x plain
                float vely = mousePos.y  - shooterY; // get distance from shooter to target on y plain
                float length = (float) Math.sqrt(velx * velx + vely * vely); // get distance to target direct
                if (length != 0) {
                    velx = velx / length;  // get required x velocity to aim at target
                    vely = vely / length;  // get required y velocity to aim at target
                }
                // create a bullet
                lvlFactory.createBullet(shooterX, shooterY, velx*speed, vely*speed);
                //reset timeSinceLastShot
                player.timeSinceLastShot = player.shootDelay;
            }
        }
    }


}
