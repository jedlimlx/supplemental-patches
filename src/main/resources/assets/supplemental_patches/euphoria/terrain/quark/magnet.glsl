if (color.r - color.g > 0.2 || color.b - color.g > 0.2) {
    if (mat % 4 == 2) {
        emission = 3.0 * pow2(max(color.r, color.b)) + 1.0;
        color.rgb *= sqrt2(color.rgb);
    }
} else {
    #include "/lib/materials/specificMaterials/terrain/stone.glsl"
}