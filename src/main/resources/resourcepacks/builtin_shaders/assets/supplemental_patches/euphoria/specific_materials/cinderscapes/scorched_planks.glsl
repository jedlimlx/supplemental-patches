smoothnessG = 12.0 * pow2(pow2(color.r));
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 1.20;
#endif