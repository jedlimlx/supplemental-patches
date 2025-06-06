smoothnessG = 1.1 * pow3(color.g);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif