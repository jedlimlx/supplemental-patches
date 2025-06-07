#include "/lib/materials/specificMaterials/planks/trumpetPlanks.glsl"

if (mat % 4 == 3) {  // Powered Redstone Components
    redstoneIPBR(color.rgb, emission);
}