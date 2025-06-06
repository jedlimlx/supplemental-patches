#ifdef GBUFFERS_TERRAIN
    smoothnessG = 6.0 * pow2(color.g) + 0.2;
#else
    smoothnessG = 6.0 * color.g + 0.2;
#endif

highlightMult = smoothnessG;
smoothnessD = smoothnessG;
materialMask = OSIEBCA * deferredMaterial("supplemental_patches:enderscape/shadoline");

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif