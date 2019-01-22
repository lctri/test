package com.tribox2dtut.entity.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.tribox2dtut.entity.components.TransformComponent;

import java.util.Comparator;

/**
 * Created by NETNAM_ADMIN on 6/27/2018.
 */

public class ZComparator implements Comparator<Entity> {
    private ComponentMapper<TransformComponent> cmTrans;

    public ZComparator(){
        cmTrans = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public int compare(Entity entityA, Entity entityB) {
        if(entityA == null || entityB == null){
            return 0;
        }
        float az = cmTrans.get(entityA).position.z;
        float bz = cmTrans.get(entityB).position.z;
        int res = 0;
        if(az > bz){
            res = 1;
        }else if(az < bz){
            res = -1;
        }
        return res;
    }

}
