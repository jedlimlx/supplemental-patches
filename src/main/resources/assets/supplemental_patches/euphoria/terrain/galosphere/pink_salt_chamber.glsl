#include "/lib/materials/specificMaterials/terrain/polishedPinkSalt.glsl"

vec2 absCoord = abs(signMidCoordPos);
if (absCoord.x < 0.2 && absCoord.y < 0.2 && CheckForColor(color.rgb, vec3(246, 216, 212))) {
    emission = 6.0;
}