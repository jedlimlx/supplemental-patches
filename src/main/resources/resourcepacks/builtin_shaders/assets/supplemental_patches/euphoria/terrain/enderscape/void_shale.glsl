#include "/lib/materials/specificMaterials/terrain/obsidian.glsl"
if (abs(NdotU) < 0.1) {
    emission = color.b > 0.27 ? 2.0 * color.b : 0.0;
}