package com.tribox2dtut.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.tribox2dtut.entity.components.B2dBodyComponent;
import com.tribox2dtut.entity.components.TransformComponent;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class PhysicsSystem extends IteratingSystem {
    private static final float MAX_STEP_TIME = 1/60f;
    private static float accumulator = 0f;

    private World world;
    private Engine engine;
    private Array<Entity> bodiesQueue;

    private ComponentMapper<B2dBodyComponent> bm = ComponentMapper.getFor(B2dBodyComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);

    @SuppressWarnings("unchecked")
    public PhysicsSystem(World world, Engine eng) {
        super(Family.all(B2dBodyComponent.class, TransformComponent.class).get());
        this.world = world;
        this.engine = eng;
        this.bodiesQueue = new Array<Entity>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if(accumulator >= MAX_STEP_TIME) {
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;

            //Entity Queue
            for (Entity entity : bodiesQueue) {
                TransformComponent tfm = tm.get(entity);
                B2dBodyComponent bodyComp = bm.get(entity);
                Vector2 position = bodyComp.body.getPosition();
                tfm.position.x = position.x;
                tfm.position.y = position.y;
                tfm.rotation = bodyComp.body.getAngle() * MathUtils.radiansToDegrees;
                //TODO check this works
                if(bodyComp.isDead){
                    System.out.println("Removing a body and entity");
                    world.destroyBody(bodyComp.body);
                    engine.removeEntity(entity);
                }

            }
        }
        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }

}
