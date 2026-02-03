#include "/lib/materials/specificMaterials/planks/darkOakPlanks.glsl"

if (color.b - color.r > 0.1) {
    emission = 1.3 * pow2(max(color.b, color.g));
}