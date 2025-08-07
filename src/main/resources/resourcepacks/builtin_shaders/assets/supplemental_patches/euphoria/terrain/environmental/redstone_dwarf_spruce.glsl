if (color.r - color.g > 0.1) {
    noSmoothLighting = true; noDirectionalShading = true;
    lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

    #include "/lib/materials/specificMaterials/terrain/redstoneTorch.glsl"
    emission += 0.0001; // No light reducing during noon

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(color, vec4(1.0, 0.0, 0.0, 1.0), emission, 5.0, lViewPos);
    #endif

    overlayNoiseIntensity = 0.0;
} else {
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
}