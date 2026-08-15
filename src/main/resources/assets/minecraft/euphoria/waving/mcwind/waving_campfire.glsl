bool base = abs(clamp(at_midBlock.y / 64.0, -2.0, 2.0)) > 0.5;
float topWeight = base ? 1.0 : 0.0;
playerPos += mcw_fireLean(blockCenter, topWeight);
