if (color.a < 0.05) {
    if (color.r > 0.78) {
        #ifdef GBUFFERS_TERRAIN
            float colorG2 = pow2(color.g);
        #else
            float colorG2 = color.g;
        #endif

        float colorG4 = pow2(colorG2);
        float factor = max(color.g, 0.8);

        smoothnessG = min1(factor - colorG4 * 0.5);
        highlightMult = 3.5 * max(colorG4, 0.2);

        color.rgb *= 0.5 + 0.4 * GetLuminance(color.rgb);
    } else {
        emission = 5.0;
    }
} else {
    translucentMultCalculated = true;
    reflectMult = 1.0;
    translucentMult.rgb = pow2(color.rgb) * 0.2;

    smoothnessG = color.r * 0.7;
    highlightMult = 2.5;
    overlayNoiseAlpha = 0.4;
    sandNoiseIntensity = 0.5;
    mossNoiseIntensity = 0.5;
}