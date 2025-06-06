noSmoothLighting = true;

if (mat % 4 == 0)
    emission = 1.5 * max0(1.2 * color.b - color.r);

color.rgb *= 1.0 + 0.7 * pow2(max(-signMidCoordPos.y + 0.8, float(NdotU > 0.9) * 1.6));

#ifdef SNOWY_WORLD
    snowFactor = 0.0;
#endif

overlayNoiseIntensity = 0.3;