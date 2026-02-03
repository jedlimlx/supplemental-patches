materialMask = OSIEBCA; // Intense Fresnel

float factor = min1(pow2(color.g) * 1.2);
float factor2 = pow2(factor);
smoothnessG = 1.0 - 0.4 * factor;
highlightMult = factor2 * 3.5;
smoothnessD = pow1_5(color.g);

#ifdef COATED_TEXTURES
    noiseFactor = 0.33;
#endif