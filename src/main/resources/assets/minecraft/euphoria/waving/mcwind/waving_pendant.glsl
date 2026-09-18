if (mcw_hasOccupancy()) {
    MCW_LOD_SKIP
    playerPos += mcw_pendantSwing(worldPos, blockCenter);
    MCW_LOD_BLEND
} else return false;
