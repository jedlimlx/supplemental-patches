smoothnessG = pow2(color.b) * 0.35;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif