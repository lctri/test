package com.tribox2dtut;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.tribox2dtut.entity.components.AnimationComponent;
import com.tribox2dtut.entity.components.B2dBodyComponent;
import com.tribox2dtut.entity.components.CollisionComponent;
import com.tribox2dtut.entity.components.EnemyComponent;
import com.tribox2dtut.entity.components.PlayerComponent;
import com.tribox2dtut.entity.components.StateComponent;
import com.tribox2dtut.entity.components.TextureComponent;
import com.tribox2dtut.entity.components.TransformComponent;
import com.tribox2dtut.entity.components.TypeComponent;
import com.tribox2dtut.simplexnoise.OpenSimplexNoise;
import com.tribox2dtut.simplexnoise.SimplexNoise;

public class LevelFactory02 {
    private BodyFactory bodyFactory;
    public World world;
    private PooledEngine engine;
    private SimplexNoise sim;
    private SimplexNoise simRough;
    public int currentLevel = 0;
    private TextureRegion floorTex;
    private TextureRegion enemyTex;
    private TextureRegion platformTex;
    private TextureRegion waterTex;
    private TextureRegion bulletTex;
    private TextureAtlas atlas;
    private OpenSimplexNoise openSim;

    public LevelFactory02(PooledEngine engine, TextureAtlas atlas) {
        this.engine = engine;
        this.atlas = atlas;
        floorTex = atlas.findRegion("reallybadlydrawndirt");
        enemyTex = atlas.findRegion("waterdrop");
        waterTex = atlas.findRegion("water");
        bulletTex = DFUtils.makeTextureRegion(10, 10, "444444FF");
        world = new World(new Vector2(0f, 10f), true);
        world.setContactListener(new B2dContactListener());
        bodyFactory = BodyFactory.getInstance(world);
        openSim = new OpenSimplexNoise(MathUtils.random(2000l));
    }

    public void generateLevel(int ylevel) {
        while (ylevel > currentLevel) {
            int range = 15;
            for (int i = 1; i < 5; i++) {
                generateSingleColumn(genNForL(i * 1,currentLevel)
                        ,genNForL(i * 100,currentLevel)
                        ,genNForL(i * 200,currentLevel)
                        ,genNForL(i * 300,currentLevel)
                        ,range,i * 10);
            }
            currentLevel++;
        }
    }

    // generate noise for level
    private float genNForL(int level, int height){
        return (float)openSim.eval(height, level);
    }

    private void generateSingleColumn(float n1, float n2,float n3,float n4, int range, int offset){
        if(n1 > -0.5f){
            createPlatform(n2 * range + offset ,currentLevel * 2);
            if(n3 > 0.3f){
                // add bouncy platform
                createBouncyPlatform(n2 * range + offset,currentLevel * 2);
            }
            if(n4 > 0.2f){
                // add an enemy
                createEnemy(enemyTex,n2 * range + offset,currentLevel * 2 + 1);
            }
        }
    }

    public Entity createEnemy(TextureRegion tex, float x, float y) {
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        EnemyComponent enemy = engine.createComponent(EnemyComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);

        b2dbody.body = bodyFactory.makeCirclePolyBody(x, y, 1, BodyFactory.STONE, BodyDef.BodyType.KinematicBody);
        position.position.set(x, y, 0);
        texture.region = tex;
        enemy.xPosCenter = x;
        type.type = TypeComponent.ENEMY;
        b2dbody.body.setUserData(entity);

        entity.add(colComp);
        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(enemy);
        entity.add(type);
        engine.addEntity(entity);

        return entity;
    }

    public Entity createBouncyPlatform(float x, float y) {
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 1f, 1f, BodyFactory.STONE, BodyDef.BodyType.StaticBody);
        bodyFactory.makeAllFixturesSensors(b2dbody.body);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = platformTex;

        TransformComponent trans = engine.createComponent(TransformComponent.class);
        trans.position.set(x, y, 0);
        entity.add(trans);

        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SPRING;

        b2dbody.body.setUserData(entity);
        entity.add(b2dbody);
        entity.add(texture);
        entity.add(type);
        engine.addEntity(entity);
        return entity;
    }

    public void createPlatform(float x, float y) {
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        b2dbody.body = bodyFactory.makeBoxPolyBody(x, y, 3f, 0.3f, BodyFactory.STONE, BodyDef.BodyType.StaticBody);
        b2dbody.body.setUserData(entity);
        entity.add(b2dbody);

        TextureComponent texture = engine.createComponent(TextureComponent.class);
        texture.region = platformTex;
        entity.add(texture);

        TypeComponent type = engine.createComponent(TypeComponent.class);
        type.type = TypeComponent.SCENERY;

        TransformComponent trans = engine.createComponent(TransformComponent.class);
        trans.position.set(x, y, 0);

        entity.add(trans);

        engine.addEntity(entity);
    }

    public void createFloor() {
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);

        position.position.set(20, 0, 0);
        texture.region = floorTex;
        type.type = TypeComponent.SCENERY;
        b2dbody.body = bodyFactory.makeBoxPolyBody(20, -16, 46, 32, BodyFactory.STONE, BodyDef.BodyType.StaticBody);

        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(type);
        b2dbody.body.setUserData(entity);
        engine.addEntity(entity);

    }

    public Entity createPlayer(TextureRegion tex, OrthographicCamera cam) {
        Entity entity = engine.createEntity();
        B2dBodyComponent b2dbody = engine.createComponent(B2dBodyComponent.class);
        TransformComponent position = engine.createComponent(TransformComponent.class);
        TextureComponent texture = engine.createComponent(TextureComponent.class);
        AnimationComponent animCom = engine.createComponent(AnimationComponent.class);
        PlayerComponent player = engine.createComponent(PlayerComponent.class);
        CollisionComponent colComp = engine.createComponent(CollisionComponent.class);
        TypeComponent type = engine.createComponent(TypeComponent.class);
        StateComponent state = engine.createComponent(StateComponent.class);

        player.cam = cam;
        b2dbody.body = bodyFactory.makeCirclePolyBody(10, 1, 1, BodyFactory.STONE, BodyDef.BodyType.DynamicBody, true);
        Animation anim = new Animation(0.1f, atlas.findRegion("flame_a"));
        animCom.animations.put(StateComponent.STATE_NORMAL, anim);
        animCom.animations.put(StateComponent.STATE_MOVING, anim);
        animCom.animations.put(StateComponent.STATE_JUMPING, anim);
        animCom.animations.put(StateComponent.STATE_FALLING, anim);
        animCom.animations.put(StateComponent.STATE_HIT, anim);

        position.position.set(10, 1, 0);
        texture.region = tex;
        type.type = TypeComponent.PLAYER;
        state.set(StateComponent.STATE_NORMAL);
        b2dbody.body.setUserData(entity);

        entity.add(b2dbody);
        entity.add(position);
        entity.add(texture);
        entity.add(animCom);
        entity.add(player);
        entity.add(colComp);
        entity.add(type);
        entity.add(state);

        engine.addEntity(entity);
        return entity;
    }

    public void createWall(TextureRegion tex) {
        
    }

}
