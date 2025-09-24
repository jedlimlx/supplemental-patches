if (GetMaxColorDif(color.rgb) > 0.05) {
    emission = 1.5 * pow2(color.b);
    color.rgb = pow1_5(color.rgb);
} else {
    #include "/lib/materials/specificMaterials/terrain/stone.glsl"
}