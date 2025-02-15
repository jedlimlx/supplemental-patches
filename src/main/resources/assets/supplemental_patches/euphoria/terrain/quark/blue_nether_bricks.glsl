float factor = color.b * 0.8;
factor = color.b > 0.3 ? factor : factor * 0.25;
smoothnessG = factor;
smoothnessD = factor;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif