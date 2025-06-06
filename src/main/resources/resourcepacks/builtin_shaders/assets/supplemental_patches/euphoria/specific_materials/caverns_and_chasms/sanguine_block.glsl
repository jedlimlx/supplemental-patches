materialMask = OSIEBCA * deferredMaterial("supplemental_patches:caverns_and_chasms/sanguine"); // Lead Fresnel
subsurfaceMode = 2;

#ifdef GBUFFERS_TERRAIN
    smoothnessG = 1.8 * pow1_5(color.r);
#else
    smoothnessG = 1.8 * color.r;
#endif

highlightMult = smoothnessG;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.60;
#endif