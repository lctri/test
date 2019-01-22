package com.tribox2dtut.entity.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.tribox2dtut.entity.components.B2dBodyComponent;
import com.tribox2dtut.entity.components.BulletComponent;
import com.tribox2dtut.entity.components.Mapper;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class BulletSystem extends IteratingSystem {
    private Entity player;

    @SuppressWarnings("unchecked")
    public BulletSystem(Entity player){
        super(Family.all(BulletComponent.class).get());
        this.player = player;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //get box 2d body and bullet components
        B2dBodyComponent b2body = Mapper.b2dCom.get(entity);
        BulletComponent bullet = Mapper.bulletCom.get(entity);

        // apply bullet velocity to bullet body
        b2body.body.setLinearVelocity(bullet.xVel, bullet.yVel);

        // get player pos
        B2dBodyComponent playerBodyComp = Mapper.b2dCom.get(player);
        float px = playerBodyComp.body.getPosition().x;
        float py = playerBodyComp.body.getPosition().y;

        //get bullet pos
        float bx = b2body.body.getPosition().x;
        float by = b2body.body.getPosition().y;

        // if bullet is 30 units away from player on any axis then it is probably off screen
        if(bx - px > 30 || by - py > 30){
            bullet.isDead = true;
        }

        //check if bullet is dead
        if(bullet.isDead){
            System.out.println("Bullet died");
            b2body.isDead = true;
        }
    }

}
