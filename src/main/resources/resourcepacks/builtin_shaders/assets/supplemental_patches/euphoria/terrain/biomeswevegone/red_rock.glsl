smoothnessG = min(pow2(pow2(color.r)) * 1.5, 0.4);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif