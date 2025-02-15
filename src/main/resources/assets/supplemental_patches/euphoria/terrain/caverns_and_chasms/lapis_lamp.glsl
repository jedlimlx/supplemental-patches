#include "/lib/materials/specificMaterials/terrain/lapisBlock.glsl"
if (color.r > 0.5) {
    emission = 3.0 * color.r;
}