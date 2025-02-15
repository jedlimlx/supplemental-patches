smoothnessG = 0.25 * pow2(color.g);
smoothnessD = smoothnessG;

mossNoiseIntensity = 0.2;

#ifdef COATED_TEXTURES
    noiseFactor = 0.60;
#endif