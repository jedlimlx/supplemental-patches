float factor;
if (mat % 8 < 4) {
    factor = color.b;
} else if (mat % 4 < 2) {
    factor = 0.8 * color.g + 0.2 * color.r;
} else if (mat % 4 == 2) {
    factor = color.g;
} else {
    factor = (color.r + color.b + color.g) / 3;
}

#include "/lib/materials/specificMaterials/terrain/corundumBlock.glsl"