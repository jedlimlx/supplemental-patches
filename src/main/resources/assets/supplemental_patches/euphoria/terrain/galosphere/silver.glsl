#include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"

if (mat % 4 == 3) {  // medium weighted pressure plate
    redstoneIPBR(color.rgb, emission);
}