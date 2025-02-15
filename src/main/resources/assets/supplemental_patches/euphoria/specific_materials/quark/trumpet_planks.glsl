smoothnessG = color.r > 0.8 ? 0.2 : pow2(color.r) * 0.7;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif