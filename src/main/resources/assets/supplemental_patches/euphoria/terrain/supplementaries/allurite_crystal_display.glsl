if (color.b > 0.7 && color.g > 0.7) {
    emission = 1.5 * sqrt(color.b) + 0.3;
} else {
    #include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
}