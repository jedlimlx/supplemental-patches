if (color.r < color.g * 3.0) { // Cerebrage Claret Stem
    #include "/lib/materials/specificMaterials/terrain/cerebrage.glsl"
    subsurfaceMode = 0;
} else { // Claret Wood
    #include "/lib/materials/specificMaterials/planks/claretPlanks.glsl"
}