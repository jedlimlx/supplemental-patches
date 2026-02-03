#include "/lib/materials/specificMaterials/terrain/ironBlock.glsl"

if (
    color.b - color.r > 0.1 || 
    max(color.r, color.g) / min(color.r, color.g) < 1.2 && color.r - color.b > 0.1
) {
    emission = 2.0 * pow2(maxOf(color.rgb));
    color.rgb = pow1_5(color.rgb);
}