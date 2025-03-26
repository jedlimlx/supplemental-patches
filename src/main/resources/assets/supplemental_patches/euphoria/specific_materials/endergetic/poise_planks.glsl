smoothnessG = color.r > 0.38 ? 1.2 * pow2(color.r) : 0.2;
smoothnessD = smoothnessG;

highlightMult = 1.0 * smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.25;
#endif