package com.tribox2dtut.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class Player02Component implements Component, Pool.Poolable {
    public OrthographicCamera cam = null;
    public boolean onPlatform = false;
    public boolean onSpring = false;
    public boolean isDead = false;
    public float shootDelay = 0.5f;
    public float timeSinceLastShot = 0f;
    @Override
    public void reset() {
        cam = null;
        onPlatform = false;
        onSpring = false;
        isDead = false;
        shootDelay = 0.5f;
        timeSinceLastShot = 0f;
    }

}
