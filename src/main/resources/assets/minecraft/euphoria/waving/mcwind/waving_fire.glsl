MCW_LOD_SKIP
// Wooden base should not sway lol
bool base = MAT_CAMPFIRE_LIT ? abs(clamp(at_midBlock.y / 64.0, -2.0, 2.0)) > 0.5 : fract(worldPos.y + 0.21) > 0.26;
float topWeight = base ? 1.0 : 0.0;
playerPos += mcw_fireLean(blockCenter, topWeight);
MCW_LOD_BLEND
