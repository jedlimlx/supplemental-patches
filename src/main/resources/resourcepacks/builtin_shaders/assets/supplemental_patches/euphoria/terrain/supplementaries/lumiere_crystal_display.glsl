if (color.r > 0.7 && color.g > 0.7) {
    emission = 1.0 * sqrt(color.r);
} else {
    #include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
}