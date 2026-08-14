if (
    color.r > 3.0 * color.b ||
    (color.r > 0.9 && abs(color.g - color.b) < 0.1 && color.r + color.g + color.b < 2.6) ||
    CheckForColor(color.rgb, vec3(251, 244, 207))
) {
    emission = 3.00;
    color.rgb *= pow(GetLuminance(color.rgb), 0.4);
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
