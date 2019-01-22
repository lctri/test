package com.tribox2dtut.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class TypeComponent implements Component, Pool.Poolable {
    public static final int PLAYER = 0;
    public static final int PLAYER02 = 7;
    public static final int ENEMY = 1;
    public static final int SCENERY = 3;
    public static final int OTHER = 4;
    public static final int SPRING = 5;
    public static final int BULLET = 6;

    public int type = OTHER;

    @Override
    public void reset() {
        type = OTHER;
    }

}
