if (color.b < 0.4 && color.r > 0.8 && color.g > 0.8) {  // Gold Part
    #include "/lib/materials/specificMaterials/terrain/goldBlock.glsl"
} else if (color.r > 0.7 && color.b > 0.7 && color.g > 0.7) {  // White Clockface
    #include "/lib/materials/specificMaterials/terrain/quartzBlock.glsl"
} else if (color.r > 0.2) {  // Wooden Part
    #include "/lib/materials/specificMaterials/planks/darkOakPlanks.glsl"
}