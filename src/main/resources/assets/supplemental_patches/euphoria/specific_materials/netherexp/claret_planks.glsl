smoothnessG = 0.5 * pow2(color.r);
smoothnessD = smoothnessG;
highlightMult = 2.0;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif