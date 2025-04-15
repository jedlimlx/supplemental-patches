noSmoothLighting = true;

if (mat % 4 == 0) {
    emission = 1.5 * color.r;

    float factor = pow2(max(-signMidCoordPos.y + 1.0, float(NdotU > 0.9) * 1.6));
    color.rgb *= 1.0 + 0.8 * factor;
    color.gb *= 1.0 + 0.2 * factor;
}

#ifdef SNOWY_WORLD
    snowFactor = 0.0;
#endif

overlayNoiseIntensity = 0.3;