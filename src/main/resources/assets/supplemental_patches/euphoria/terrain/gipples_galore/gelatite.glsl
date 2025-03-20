smoothnessG = pow2(color.b + color.r) * (mat % 4 < 2 ? 0.3 : 0.4);
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif