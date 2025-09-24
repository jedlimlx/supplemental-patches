materialMask = OSIEBCA; // Intense Fresnel
smoothnessG = 0.8 * max(color.r / color.b, color.b / color.r) * pow3(maxAll(color.rgb));
smoothnessD = smoothnessG;

color.rgb *= 0.6 + 0.7 * GetLuminance(color.rgb);

#if defined COATED_TEXTURES && defined GBUFFERS_TERRAIN
    noiseFactor = 0.5;
#endif