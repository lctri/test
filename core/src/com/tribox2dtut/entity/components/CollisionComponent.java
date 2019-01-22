package com.tribox2dtut.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class CollisionComponent implements Component, Pool.Poolable {
    public Entity collisionEntity;

    @Override
    public void reset() {
        collisionEntity = null;
    }

}
