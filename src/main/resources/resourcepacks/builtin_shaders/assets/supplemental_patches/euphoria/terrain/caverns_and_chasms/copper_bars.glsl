#include "/lib/materials/specificMaterials/terrain/copperBlock.glsl"

if (mat % 4 == 1) {
    noSmoothLighting = true;
} else if (mat % 4 == 3) {
    redstoneIPBR(color.rgb, emission);
}