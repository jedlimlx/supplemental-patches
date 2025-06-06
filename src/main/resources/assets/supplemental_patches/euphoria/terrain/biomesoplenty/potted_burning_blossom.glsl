noSmoothLighting = true;

float NdotE = dot(normalM, eastVec);
if (abs(abs(NdotE) - 0.5) < 0.4) {
    subsurfaceMode = 1, noDirectionalShading = true, isFoliage = true;

    if (color.r > 0.78 || color.r - color.g > 0.1) {
        emission = 5.0 * color.r * color.g;
        color.rgb *= color.rgb;
        #ifdef GBUFFERS_TERRAIN
            lmCoordM.x += 0.5 - 0.7 * pow2(length(signMidCoordPos));
        #endif
    }
}