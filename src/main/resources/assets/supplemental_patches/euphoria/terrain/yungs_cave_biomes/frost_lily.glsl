materialMask = OSIEBCA; // Intense Fresnel

float factor = pow2(color.g);
float factor2 = pow2(factor);
smoothnessG = 1.0 - 0.5 * factor;
highlightMult = factor2 * 3.5;
smoothnessD = factor;

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif

#ifdef SSS_SNOW_ICE
    subsurfaceMode = 3, noSmoothLighting = true, noDirectionalShading = true;
#endif

noSmoothLighting = true;
lmCoordM.x *= 0.85;

emission = 1.3 * pow2(color.r);
overlayNoiseIntensity = 0.5;
