if (color.b - color.r > 0.15 || color.b > 0.98) {
    emission = 2.1;
    color.rgb *= sqrt1(GetLuminance(color.rgb));
} else {
    lmCoordM.x *= 0.88;

    if (mat % 4 < 2) {
        smoothnessG = color.r * 0.2;
        smoothnessD = smoothnessG;

        #ifdef GBUFFERS_TERRAIN
            DoBrightBlockTweaks(color.rgb, 0.5, shadowMult, highlightMult);
        #endif
    } else {
        smoothnessG = color.r * 2.3;
        smoothnessD = smoothnessG;
    }

    #ifdef COATED_TEXTURES
        noiseFactor = 0.33;
    #endif
}