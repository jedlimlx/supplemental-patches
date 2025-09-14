subsurfaceMode = 1, noSmoothLighting = true, noDirectionalShading = true;

#ifdef GBUFFERS_TERRAIN
    DoFoliageColorTweaks(color.rgb, shadowMult, snowMinNdotU, viewPos, nViewPos, lViewPos, dither);

    #ifdef COATED_TEXTURES
        doTileRandomisation = false;
    #endif
#endif

if (color.r > 0.54) {
    #if defined GBUFFERS_TERRAIN
        emission = (1.0 - abs(signMidCoordPos.x)) * max0(0.7 - abs(signMidCoordPos.y + 0.5));
        emission = pow1_5(emission) * 3.0;
    #else
        emission = color.r;
    #endif
}

sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0;