materialMask = OSIEBCA; // Intense Fresnel

float factor = 1.2 * GetLuminance(color.rgb) + 0.5;
float factor3 = pow3(factor);

smoothnessG = factor - factor3 * 0.4;
highlightMult = 0.5 * factor3;

smoothnessD = factor3 * 0.75;

#ifdef COATED_TEXTURES
    noiseFactor = 0.5;
#endif