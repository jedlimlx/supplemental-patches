if (GetMaxColorDif(color.rgb) < 0.01) {
    #include "/lib/materials/specificMaterials/terrain/ironBlock.glsl"
} else {
    emission = color.g > 0.98 ? 2.5 * pow2(color.b) : 0.0;
}