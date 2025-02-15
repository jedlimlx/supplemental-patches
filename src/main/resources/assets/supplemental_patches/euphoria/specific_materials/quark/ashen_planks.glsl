smoothnessG = pow2(pow2(color.r)) * 0.1;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.60;
#endif