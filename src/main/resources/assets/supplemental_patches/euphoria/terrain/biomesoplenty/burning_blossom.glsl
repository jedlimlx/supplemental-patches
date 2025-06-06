subsurfaceMode = 1, noDirectionalShading = true, isFoliage = true;

if (color.r > 0.78 || color.r - color.g > 0.1) {
    emission = 5.0 * color.r * color.g;
    color.rgb *= color.rgb;
    #ifdef GBUFFERS_TERRAIN
        lmCoordM.x += 0.5 - 0.7 * pow2(length(signMidCoordPos));
        lmCoordM.x = min1(lmCoordM.x);
    #endif
}

sandNoiseIntensity = 0.0, mossNoiseIntensity = 0.0;