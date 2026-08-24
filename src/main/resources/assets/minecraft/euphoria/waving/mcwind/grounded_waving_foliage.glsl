float h = mcw_grassHeight(worldPos, blockCenter, mcw_groundHeight(blockCenter, cameraPosition));
playerPos.xz += mcw_grassPush(blockCenter, h);
