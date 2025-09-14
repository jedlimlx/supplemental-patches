if (color.r > color.b) { //netherrack
    #include "/lib/materials/specificMaterials/terrain/netherrack.glsl"
} else {
    smoothnessG = 0.5 * color.r;
    smoothnessD = smoothnessG;

    #ifdef COATED_TEXTURES
        noiseFactor = 0.77;
    #endif
}