materialMask = OSIEBCA; // Intense Fresnel

float factor = pow2(color.r);
highlightMult = factor * 3.0;
color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);

smoothnessG = 0.8 - factor * 0.3;
smoothnessD = factor;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif