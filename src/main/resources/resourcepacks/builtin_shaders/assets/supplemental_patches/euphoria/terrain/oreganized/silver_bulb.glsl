#include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"

if (color.b > 0.5 && color.b > 1.8 * color.r) {
    emission = 0.5 * color.b + 0.2;
}
