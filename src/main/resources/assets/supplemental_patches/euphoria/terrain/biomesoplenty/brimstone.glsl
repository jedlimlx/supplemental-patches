smoothnessG = 0.3 * pow3(color.r);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif