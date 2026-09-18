MCW_LOD_SKIP
float h = mcw_grassHeight(worldPos, blockCenter, 0.0);
playerPos.xz += mcw_grassPush(blockCenter, h);
playerPos.xz += mcw_draftPush(blockCenter, cameraPosition, h);
float dn = mcw_draftDown(blockCenter, cameraPosition);
if (dn > 0.0) {
    playerPos.y -= dn * h * MCW_DOWNWASH_PRESS;
    vec2 out2 = worldPos.xz - blockCenter.xz;
    float ol = length(out2);
    if (ol > 1.0e-4) {
        playerPos.xz += (out2 / ol) * dn * h * MCW_DOWNWASH_SPLAY;
    }
}
MCW_LOD_BLEND
