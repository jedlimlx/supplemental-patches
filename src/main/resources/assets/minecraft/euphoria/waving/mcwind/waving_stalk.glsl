float groundY = mcw_groundHeight(blockCenter, cameraPosition);
playerPos.xz += mcw_stalkSway(worldPos, blockCenter, groundY);
