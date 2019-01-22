package com.tribox2dtut.views;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tribox2dtut.Box2DTutorial;
import com.tribox2dtut.DFUtils;
import com.tribox2dtut.LevelFactory;
import com.tribox2dtut.controller.KeyboardController;
import com.tribox2dtut.entity.components.PlayerComponent;
import com.tribox2dtut.entity.systems.AnimationSystem;
import com.tribox2dtut.entity.systems.BulletSystem;
import com.tribox2dtut.entity.systems.CollisionSystem;
import com.tribox2dtut.entity.systems.EnemySystem;
import com.tribox2dtut.entity.systems.LevelGenerationSystem;
import com.tribox2dtut.entity.systems.PhysicsDebugSystem;
import com.tribox2dtut.entity.systems.PhysicsSystem;
import com.tribox2dtut.entity.systems.Player02ControlSystem;
import com.tribox2dtut.entity.systems.PlayerControlSystem;
import com.tribox2dtut.entity.systems.RenderingSystem;
import com.tribox2dtut.entity.systems.WallSystem;
import com.tribox2dtut.entity.systems.WaterFloorSystem;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class MainScreen implements Screen {
    private Box2DTutorial parent;
    private OrthographicCamera cam;
    private KeyboardController controller;
    private SpriteBatch sb;
    private PooledEngine engine;
    private LevelFactory lvlFactory;

    private Sound ping;
    private Sound boing;
    private TextureAtlas atlas;
    private Entity player;
    private Entity player02;


    /**
     * @param box2dTutorial
     */
    public MainScreen(Box2DTutorial box2dTutorial) {
        parent = box2dTutorial;
        parent.assMan.queueAddSounds();
        parent.assMan.manager.finishLoading();
        atlas = parent.assMan.manager.get("images/game.atlas", TextureAtlas.class);
        ping = parent.assMan.manager.get("sounds/ping.wav",Sound.class);
        boing = parent.assMan.manager.get("sounds/boing.wav",Sound.class);
        controller = new KeyboardController();
        engine = new PooledEngine();
        // next guide - changed this to atlas
        lvlFactory = new LevelFactory(engine,atlas);

        sb = new SpriteBatch();
        RenderingSystem renderingSystem = new RenderingSystem(sb);
        cam = renderingSystem.getCamera();
        sb.setProjectionMatrix(cam.combined);

        engine.addSystem(new AnimationSystem());
        engine.addSystem(new PhysicsSystem(lvlFactory.world, engine));
        engine.addSystem(renderingSystem);
        engine.addSystem(new PhysicsDebugSystem(lvlFactory.world, renderingSystem.getCamera()));
        engine.addSystem(new CollisionSystem());
        engine.addSystem(new PlayerControlSystem(controller,lvlFactory));
        engine.addSystem(new Player02ControlSystem(controller,lvlFactory));
        engine.addSystem(new EnemySystem());
        player = lvlFactory.createPlayer(atlas.findRegion("player"),cam);
        player02 = lvlFactory.createPlayer02(atlas.findRegion("player"),cam);
        engine.addSystem(new WallSystem(player));
        engine.addSystem(new WaterFloorSystem(player));
        engine.addSystem(new BulletSystem(player));
        engine.addSystem(new LevelGenerationSystem(lvlFactory));

        lvlFactory.createFloor();
        lvlFactory.createWaterFloor();

        int wallWidth = (int) (1*RenderingSystem.PPM);
        int wallHeight = (int) (60*RenderingSystem.PPM);
        TextureRegion wallRegion = DFUtils.makeTextureRegion(wallWidth, wallHeight, "222222FF");
        lvlFactory.createWalls(wallRegion); //TODO make some damn images for this stuff
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(delta);

        //check if player is dead. if so show end screen
        PlayerComponent pc = (player.getComponent(PlayerComponent.class));
        if(pc.isDead){
            DFUtils.log("YOU DIED : back to menu you go!");
            parent.lastScore = (int) pc.cam.position.y;
            parent.changeScreen(Box2DTutorial.ENDGAME);
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }


}
