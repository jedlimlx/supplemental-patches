#include "/lib/materials/specificMaterials/terrain/spinelBlock.glsl"
if (color.g > 0.78) {
    emission = 3.0 * color.g;
}