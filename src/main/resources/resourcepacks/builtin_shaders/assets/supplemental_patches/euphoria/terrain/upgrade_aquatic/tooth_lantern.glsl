if (color.b > color.r + 0.1) {
    noSmoothLighting = true;
    emission = pow2(color.b) * 4.0;
    color.rgb *= color.rgb;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
    #endif
} else {
    lmCoordM.x *= 0.88;

    smoothnessG = color.r * 0.2;
    smoothnessD = smoothnessG;

    #ifdef GBUFFERS_TERRAIN
        DoBrightBlockTweaks(color.rgb, 0.5, shadowMult, highlightMult);
    #endif

    #ifdef COATED_TEXTURES
        noiseFactor = 0.33;
    #endif
}