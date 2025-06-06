materialMask = OSIEBCA; // Intense Fresnel

smoothnessG = color.r > 0.4 ? 0.6 : 0.1;
smoothnessD = smoothnessG;
highlightMult = 2.0 + smoothnessG * 3.0;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif