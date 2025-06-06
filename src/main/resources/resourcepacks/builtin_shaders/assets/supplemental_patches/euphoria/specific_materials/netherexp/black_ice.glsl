materialMask = OSIEBCA * deferredMaterial("supplemental_patches:netherexp/black_ice"); // Intense Fresnel

float factor = 3.0 * pow2(0.4 - color.r);
float factor2 = pow2(factor);
smoothnessG = factor;
smoothnessD = smoothnessG;
highlightMult = factor2 * 1.5;

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif

#ifdef SSS_SNOW_ICE
    subsurfaceMode = 3, noSmoothLighting = true, noDirectionalShading = true;
#endif