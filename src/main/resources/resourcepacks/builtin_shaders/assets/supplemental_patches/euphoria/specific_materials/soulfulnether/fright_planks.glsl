smoothnessG = 0.25 * pow2(color.b);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.60;
#endif