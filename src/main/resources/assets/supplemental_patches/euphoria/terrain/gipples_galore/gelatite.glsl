smoothnessG = pow2((color.r + color.b) / 2) * 0.3;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif