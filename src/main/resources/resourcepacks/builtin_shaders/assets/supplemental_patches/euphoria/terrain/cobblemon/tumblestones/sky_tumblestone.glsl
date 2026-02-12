materialMask = OSIEBCA; // Intense Fresnel

float factor = 0.9 * pow2(color.b);
highlightMult = factor * 3.0;
color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);

smoothnessG = 0.8 - factor * 0.3;
smoothnessD = factor;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif