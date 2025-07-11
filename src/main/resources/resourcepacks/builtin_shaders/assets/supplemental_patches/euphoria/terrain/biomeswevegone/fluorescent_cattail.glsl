if (
    CheckForColor(color.rgb, vec3(99, 140, 44)) ||
    CheckForColor(color.rgb, vec3(55, 103, 38)) ||
    CheckForColor(color.rgb, vec3(43, 78, 29)) ||
    CheckForColor(color.rgb, vec3(25, 57, 22))
) {
    subsurfaceMode = 1, noSmoothLighting = true, noDirectionalShading = true;

    #ifdef GBUFFERS_TERRAIN
        DoFoliageColorTweaks(color.rgb, shadowMult, snowMinNdotU, viewPos, nViewPos, lViewPos, dither);

        #ifdef COATED_TEXTURES
        doTileRandomisation = false;
        #endif
    #endif

    #if SHADOW_QUALITY == -1
        shadowMult *= 1.0 - 0.3 * (signMidCoordPos.y + 1.0) * (1.0 - abs(signMidCoordPos.x))
                   + 0.5 * (1.0 - signMidCoordPos.y) * invNoonFactor;  // consistency357381
    #endif

    sandNoiseIntensity = 0.8, mossNoiseIntensity = 0.0, isFoliage = true;
} else {
    float dotColor = dot(color.rgb, color.rgb);
    emission = 0.3 * dotColor + 0.7;

    overlayNoiseIntensity = 0.3;
}