noSmoothLighting = true;
#include "/lib/materials/specificMaterials/planks/frightPlanks.glsl"

if (mat % 4 == 3) {  // Powered Redstone Components
    redstoneIPBR(color.rgb, emission);
}