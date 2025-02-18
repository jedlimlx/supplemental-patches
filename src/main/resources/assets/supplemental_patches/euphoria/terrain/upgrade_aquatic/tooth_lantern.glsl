if (color.b > color.r + 0.1) {
    noSmoothLighting = true;
    emission = pow2(color.b) * 1.7;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
    #endif
} else {
    smoothnessG = color.r * 0.2;
    smoothnessD = smoothnessG;

    #ifdef GBUFFERS_TERRAIN
        DoBrightBlockTweaks(color.rgb, 0.5, shadowMult, highlightMult);
    #endif

    #ifdef COATED_TEXTURES
        noiseFactor = 0.33;
    #endif
}