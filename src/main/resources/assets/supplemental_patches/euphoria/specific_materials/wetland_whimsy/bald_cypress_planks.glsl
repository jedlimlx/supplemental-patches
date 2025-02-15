if (color.r > color.b) {
    #include "/lib/materials/specificMaterials/terrain/copperBlock.glsl"
} else {
    smoothnessG = 0.7 * pow2(color.b);
    smoothnessD = smoothnessG;

    #ifdef COATED_TEXTURES
        noiseFactor = 0.45;
    #endif
}