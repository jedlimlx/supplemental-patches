materialMask = OSIEBCA * deferredMaterial("supplemental_patches:oreganized/lead"); // Lead Fresnel

#ifdef GBUFFERS_TERRAIN
    smoothnessG = 1.3 * pow2(color.b);
#else
    smoothnessG = 1.3 * color.b;
#endif

highlightMult = 2.5 * min1(smoothnessG);
smoothnessD = smoothnessG;

color.rgb *= 0.4 + 0.8 * GetLuminance(color.rgb);

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif