#ifdef GBUFFERS_TERRAIN
    smoothnessG = 3.0 * pow2(pow2(color.b));
#else
    smoothnessG = 3.0 * pow2(color.b);
#endif
highlightMult = smoothnessG;
smoothnessD = smoothnessG;
materialMask = OSIEBCA; // Intense Fresnel

color.rgb *= 0.6 + 0.7 * GetLuminance(color.rgb);

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif