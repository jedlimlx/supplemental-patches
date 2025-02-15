noSmoothLighting = true; noDirectionalShading = true;
lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

if (color.g - color.r > 0.1 || color.b > 0.8) {
    emission = 3.0;

    overlayNoiseIntensity = 0.0;
}
#ifdef GBUFFERS_TERRAIN
else if (abs(NdotU) < 0.5) {
    #if MC_VERSION >= 12102 // torch model got changed in 1.21.2
        lmCoordM.x = min1(0.7 + 0.3 * smoothstep1(max0(0.4 - signMidCoordPos.y)));
    #else
        lmCoordM.x = min1(0.7 + 0.3 * pow2(1.0 - signMidCoordPos.y));
    #endif
}
#else
else {
    color.rgb *= 1.5;
}
#endif

emission += 0.0001; // No light reducing during noon