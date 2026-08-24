if (mcw_hasOccupancy()) {
    float weld = mcw_leafWeld(worldPos, blockCenter);
    vec3 delta = mcw_leafSway(worldPos, blockCenter, weld);
    delta.xz += mcw_vineSwing(worldPos, blockCenter, weld);
    playerPos += mcw_vineDrop(delta, worldPos, blockCenter);
} else return false;
