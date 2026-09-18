MCW_LOD_SKIP
float groundY = mcw_groundHeight(blockCenter, cameraPosition);
playerPos.xz += mcw_stalkSway(worldPos, blockCenter, groundY);
MCW_LOD_BLEND
