if (color.r - color.b > 0.25) {
    #include "/lib/materials/specificMaterials/terrain/leadBlock.glsl"
} else {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
}