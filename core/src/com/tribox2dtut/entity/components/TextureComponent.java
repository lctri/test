package com.tribox2dtut.entity.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class TextureComponent implements Component, Pool.Poolable {
    public TextureRegion region = null;

    @Override
    public void reset() {
        region = null;
    }

}
