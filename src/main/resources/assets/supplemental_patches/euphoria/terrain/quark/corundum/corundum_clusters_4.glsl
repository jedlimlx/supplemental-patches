float factor;
if (mat % 2 == 0) {
   factor = color.g;
} else {
   factor = (color.r + color.b + color.g) / 3;
}

#include "/lib/materials/specificMaterials/terrain/corundumCluster.glsl"