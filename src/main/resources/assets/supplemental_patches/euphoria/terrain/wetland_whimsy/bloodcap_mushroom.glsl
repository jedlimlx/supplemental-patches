#if GLOWING_BLOODCAP_MUSHROOM == 1
    emission = color.r;
    overlayNoiseEmission = 0.4;
#elif GLOWING_BLOODCAP_MUSHROOM == 2
    emission = 1.5 * color.r;
    overlayNoiseEmission = 0.4;
#elif GLOWING_BLOODCAP_MUSHROOM == 3
    emission = 2.0 * color.r;
    overlayNoiseEmission = 0.4;
#endif

overlayNoiseIntensity = 0.6;