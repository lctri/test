package com.tribox2dtut.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class EnemyComponent implements Component, Pool.Poolable {
    public boolean isDead = false;
    public float xPosCenter = -1;
    public boolean isGoingLeft = false;
    @Override
    public void reset() {
        isDead = false;
        xPosCenter = -1;
        isGoingLeft = false;
    }

}
