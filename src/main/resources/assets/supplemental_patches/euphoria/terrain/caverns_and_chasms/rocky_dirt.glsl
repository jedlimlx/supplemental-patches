if (abs(color.r - color.b) < 0.02) {
    #include "/lib/materials/specificMaterials/terrain/stone.glsl"
} else {
    #include "/lib/materials/specificMaterials/terrain/dirt.glsl"
}