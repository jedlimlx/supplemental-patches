MCW_LOD_SKIP
// A snow layer sitting directly on a leaf canopy should ride the leaf's own sway rather
// than stand still above it. mcw_readCanopy publishes the leaf surface height per column;
// if the block right below this snow matches that height, anchor the snow to the SAME
// sway calculation the leaf underneath it uses.
float supportY = floor(blockCenter.y) - 1.0;
vec3 leafCenter = vec3(floor(blockCenter.x) + 0.5, supportY + 0.5, floor(blockCenter.z) + 0.5);
mcw_Canopy canopy = mcw_readCanopy(leafCenter.xz);
if (canopy.known && abs(supportY - canopy.y) < 0.1) {
    vec3 snowAnchor = vec3(worldPos.x, leafCenter.y, worldPos.z);
    float weld = Weld_MCWIND(snowAnchor);
    playerPos += mcw_leafSway(snowAnchor, leafCenter, weld);
}
MCW_LOD_BLEND
