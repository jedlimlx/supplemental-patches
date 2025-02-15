smoothnessG = min(pow3(color.r) * 2.5, 1.0);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif