if (color.r > 0.95 && mat % 8 == 0) {
    noSmoothLighting = true;
    lmCoordM.x = 1.0;
    emission = GetLuminance(color.rgb) * 4.1;
    #ifndef GBUFFERS_TERRAIN
        emission *= 0.65;
    #endif

    color.r *= 1.4;
    color.b *= 0.5;
    overlayNoiseIntensity = 0.0;

    #ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
        if (color.g > 0.999) color.rgb = changeColorFunction(color.rgb, 1.5, colorSoul, inSoulValley);
    #endif
    #ifdef PURPLE_END_FIRE_INTERNAL
        if (color.g > 0.5) color.rgb = changeColorFunction(color.rgb, 2.0, colorEndBreath, 1.0);
    #endif

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(color, vec4(1.0, 0.6, 0.2, 1.0), emission, 5.0, lViewPos);
    #endif
} else if (color.b > 0.6 && mat % 8 == 2) {
    noSmoothLighting = true; noDirectionalShading = true;
    lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

    emission = 2.7;
    color.rgb = pow1_5(color.rgb);
    color.r = min1(color.r + 0.1);

    overlayNoiseIntensity = 0.0;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(color, vec4(0.5, 1.0, 1.0, 1.0), emission, 3.0, lViewPos);
    #endif
} else if (color.b > 0.95 && mat % 8 == 4) {
    noSmoothLighting = true; noDirectionalShading = true;
    lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);
    emission = 3.0;

    overlayNoiseIntensity = 0.0;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 3.0, lViewPos);
    #endif
} else if (color.g > 0.6 && mat % 8 == 6) {
    noSmoothLighting = true; noDirectionalShading = true;
    lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

    emission = 3.0;
    color.rgb = pow2(color.rgb);
    color.r = min1(color.r + 0.1);

    overlayNoiseIntensity = 0.0;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 5.0, lViewPos);
    #endif
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