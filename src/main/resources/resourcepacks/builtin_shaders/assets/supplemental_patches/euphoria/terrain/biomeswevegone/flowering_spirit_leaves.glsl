#include "/lib/materials/specificMaterials/terrain/leaves.glsl"
if (color.b > 0.77 || color.b - color.g > 0.1) {
    emission = 0.5 * color.b;
}