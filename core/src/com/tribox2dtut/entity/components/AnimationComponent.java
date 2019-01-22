package com.tribox2dtut.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class AnimationComponent implements Component, Pool.Poolable{
    public IntMap<Animation> animations = new IntMap<Animation>();

    @Override
    public void reset() {
        animations = new IntMap<Animation>();
    }

}
