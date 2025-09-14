smoothnessG = 0.5 * pow2(color.r);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.8;
#endif