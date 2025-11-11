#if GLOWING_BLOODCAP_MUSHROOM == 1
    emission = color.r;
#elif GLOWING_BLOODCAP_MUSHROOM == 2
    emission = 1.5 * color.r;
#elif GLOWING_BLOODCAP_MUSHROOM == 3
    emission = 2.0 * color.r;
#endif

translucentMultCalculated = true;
reflectMult = max0(0.2 - emission);
translucentMult.rgb = pow2(color.rgb);

highlightMult = 2.5;
overlayNoiseAlpha = 0.4;
sandNoiseIntensity = 0.5;
mossNoiseIntensity = 0.5;