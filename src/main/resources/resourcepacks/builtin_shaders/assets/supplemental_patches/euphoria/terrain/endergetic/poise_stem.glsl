#include "/lib/materials/specificMaterials/planks/poisePlanks.glsl"

if (mat % 4 == 2) {
    if (color.r < 0.4 || CheckForColor(color.rgb, vec3(124, 67, 125))) {
        lmCoordM.x *= 0.88;
    } else {
        emission = 2.0 * pow2(min1(color.r * 1.8));
        color.rgb *= pow(color.rgb, vec3(emission * 0.4));
    }
}