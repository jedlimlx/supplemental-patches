materialMask = OSIEBCA * deferredMaterial("supplemental_patches:oreganized/electrum"); // Electrum Fresnel

#ifdef GBUFFERS_TERRAIN
    float colorG2 = pow2(color.g);
#else
    float colorG2 = color.g;
#endif
float colorG4 = pow2(colorG2);
float factor = max(color.g, 0.9);

smoothnessG = 1.2 * min1(factor - colorG4 * 0.8);
highlightMult = 3.0 * max(colorG4, 0.4);

smoothnessD = 1.2 * colorG4;

color.rgb *= 0.5 + 0.4 * GetLuminance(color.rgb);

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif