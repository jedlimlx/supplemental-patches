smoothnessG = pow2(color.r) * 0.4 + 0.1;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif