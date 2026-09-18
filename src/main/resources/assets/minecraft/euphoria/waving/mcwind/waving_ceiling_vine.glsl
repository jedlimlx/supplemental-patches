if (mcw_hasOccupancy()) {
    MCW_LOD_SKIP
    float weld = Weld_MCWIND(worldPos);
    vec3 delta = mcw_leafSway(worldPos, blockCenter, weld);
    delta.xz += mcw_vineSwing(worldPos, blockCenter, weld);
    playerPos += mcw_vineDrop(delta, worldPos, blockCenter);
    MCW_LOD_BLEND
} else return false;
