materialMask = OSIEBCA; // Intense Fresnel

float factor = pow2(color.g);
float factor2 = pow2(factor);
smoothnessG = 1.0 - 0.7 * factor;
highlightMult = factor2 * 3.0;
smoothnessD = factor;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif

#ifdef SSS_SNOW_ICE
    subsurfaceMode = 3, noSmoothLighting = true, noDirectionalShading = true;
#endif