subsurfaceMode = 1, noSmoothLighting = true, noDirectionalShading = true;

#ifdef GBUFFERS_TERRAIN
    DoFoliageColorTweaks(color.rgb, shadowMult, snowMinNdotU, viewPos, nViewPos, lViewPos, dither);

    #ifdef COATED_TEXTURES
        doTileRandomisation = false;
    #endif
#endif

#if SHADOW_QUALITY == -1
    shadowMult *= 1.0 - 0.3 * (signMidCoordPos.y + 1.0) * (1.0 - abs(signMidCoordPos.x))
                + 0.5 * (1.0 - signMidCoordPos.y) * invNoonFactor; // consistency357381
#endif

sandNoiseIntensity = 0.8, mossNoiseIntensity = 0.0, isFoliage = true;

if (color.b > color.g && color.b > color.r) {
    emission = 1.8 * pow2(color.b) + 0.5;
    color.rg *= color.rg;
    maRecolor = vec3(0.1);
}
    