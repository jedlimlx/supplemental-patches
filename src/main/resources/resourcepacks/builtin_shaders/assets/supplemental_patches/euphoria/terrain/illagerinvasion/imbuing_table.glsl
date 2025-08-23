if (color.b - color.r > 0.05) {  // Emissive
    emission = 0.6;
} else if (color.r > 0.62) {  // Copper
    #include "/lib/materials/specificMaterials/terrain/copperBlock.glsl"
} else {  // Wood
    #include "/lib/materials/specificMaterials/planks/darkOakPlanks.glsl"
}