#include "/lib/materials/specificMaterials/terrain/leaves.glsl"
lmCoordM.x *= 0.77;

if (color.r > 0.75 && color.g > 0.75 && !CheckForColor(color.rgb, vec3(237, 192, 102))) {
    emission = 0.5 * smoothstep1(min1(5.0 * (color.r - 0.75)));
}