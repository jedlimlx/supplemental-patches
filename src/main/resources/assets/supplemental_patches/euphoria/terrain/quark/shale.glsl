float factor = pow2(color.b);
float factor2 = pow2(factor);
smoothnessG = 0.6 * (1 - factor);
highlightMult = factor2 * 3.0;
smoothnessD = factor;

#ifdef COATED_TEXTURES
    noiseFactor = 0.45;
#endif