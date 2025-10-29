#include "/lib/materials/specificMaterials/planks/sprucePlanks.glsl"

if (color.b > color.r || color.b > 0.6) {
    emission = 1.8 * pow2(color.b) + 0.5;
    color.rg *= color.rg;
    maRecolor = vec3(0.1);
} else {
    lmCoordM.x *= 0.77;
}