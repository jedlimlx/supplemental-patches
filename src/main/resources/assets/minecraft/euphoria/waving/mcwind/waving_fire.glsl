bool base = fract(worldPos.y + 0.21) > 0.26;
float topWeight = base ? 1.0 : 0.0;
playerPos += mcw_fireLean(blockCenter, topWeight);
