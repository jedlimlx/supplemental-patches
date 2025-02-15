if (color.r > 0.7 && color.b > 0.7) {
    emission = 1.5 * sqrt(color.r);
} else {
    #include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
}