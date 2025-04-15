#include "/lib/materials/specificMaterials/planks/claretPlanks.glsl"

if (mat % 4 == 3) {  // Powered Redstone Components
    redstoneIPBR(color.rgb, emission);
}