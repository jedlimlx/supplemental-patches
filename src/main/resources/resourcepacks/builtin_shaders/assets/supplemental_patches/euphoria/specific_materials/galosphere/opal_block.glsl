materialMask = OSIEBCA; // Intense Fresnel

float factor = 1.5 * color.b;
float factor2 = pow2(factor);

smoothnessG = factor;
smoothnessD = factor;
highlightMult = 3.0 * max(factor2, 0.2);

color.rgb *= 0.7 + 0.4 * GetLuminance(color.rgb);

#ifdef COATED_TEXTURES
    noiseFactor = 0.5;
#endif