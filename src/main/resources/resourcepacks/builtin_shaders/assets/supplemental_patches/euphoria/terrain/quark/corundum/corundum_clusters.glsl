float factor;
if (mat % 16 < 8) {
    factor = color.b;
} else if (mat % 8 < 4) {
    factor = 0.8 * color.g + 0.2 * color.r;
} else if (mat % 2 == 0) {
    factor = color.g;
} else {
    factor = (color.r + color.b + color.g) / 3;
}

#include "/lib/materials/specificMaterials/terrain/corundumCluster.glsl"