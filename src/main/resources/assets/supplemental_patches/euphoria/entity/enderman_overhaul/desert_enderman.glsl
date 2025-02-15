highlightMult = 2.0;
smoothnessG = pow2(pow2(color.g)) * 0.5;
smoothnessG = min1(smoothnessG);
smoothnessD = smoothnessG * 0.7;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif