smoothnessG = 0.1 + 0.1 * color.r + 0.03 * color.b;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif