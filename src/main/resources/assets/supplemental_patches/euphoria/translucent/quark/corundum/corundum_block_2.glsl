float factor;
if (mat % 2 < 2) {
    factor = 0.8 * color.g + 0.2 * color.r;
} else if (mat % 2 == 2) {
    factor = color.g;
} else {
    factor = (color.r + color.b + color.g) / 3;
}

#include "/lib/materials/specificMaterials/terrain/corundumBlock.glsl"