emission = 1.2 * pow2(color.b);

smoothnessG = pow2(color.g) * 0.8;
highlightMult = 1.5;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif