materialMask = OSIEBCA; // Intense Fresnel

float factor = pow2(color.b);
float factor2 = pow2(factor);
smoothnessG = 1.3 - 0.7 * factor;
highlightMult = factor2 * 3.0;
smoothnessD = factor;

#ifdef COATED_TEXTURES
    noiseFactor = 0.20;
#endif