float factor = 1.2 * color.b - 0.2 * color.r;
#ifdef GBUFFERS_TERRAIN
    smoothnessG = 3.0 * pow2(pow2(factor));
#else
    smoothnessG = 3.0 * pow2(factor);
#endif
highlightMult = smoothnessG;
smoothnessD = smoothnessG;
materialMask = OSIEBCA; // Intense Fresnel

color.rgb *= 0.6 + 0.7 * GetLuminance(color.rgb);

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif
