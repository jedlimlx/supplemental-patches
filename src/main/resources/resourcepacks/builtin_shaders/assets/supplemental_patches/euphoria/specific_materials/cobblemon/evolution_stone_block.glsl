materialMask = OSIEBCA; // Intense Fresnel

float factor = max(maxOf(color), 0.8);
float factor2 = pow2(factor);
#ifdef GBUFFERS_TERRAIN
    float factor4 = pow2(factor2);
#else
    float factor4 = factor2;
#endif

smoothnessG = factor - pow2(pow2(maxOf(color))) * 0.4;
highlightMult = 3.0 * max(pow2(factor4), 0.2);

smoothnessD = factor4 * 0.75;

color.rgb *= 0.7 + 0.4 * GetLuminance(color.rgb);

#ifdef COATED_TEXTURES
    noiseFactor = 0.5;
#endif