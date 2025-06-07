if (color.r - color.b > 0.1) {  // cerebrage
    #include "/lib/materials/specificMaterials/terrain/cerebrage.glsl"
} else {  // bone
    smoothnessG = color.r * 0.2;
    smoothnessD = smoothnessG;

    #ifdef GBUFFERS_TERRAIN
        DoBrightBlockTweaks(color.rgb, 0.5, shadowMult, highlightMult);
    #endif

    #ifdef COATED_TEXTURES
        noiseFactor = 0.33;
    #endif
}