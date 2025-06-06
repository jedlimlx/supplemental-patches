smoothnessG = 0.5 * pow2(color.g) + 0.05;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif