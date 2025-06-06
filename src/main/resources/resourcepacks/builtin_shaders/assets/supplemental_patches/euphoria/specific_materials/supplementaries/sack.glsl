smoothnessG = pow(color.r, 6.0) * 0.7;
smoothnessD = smoothnessG;

overlayNoiseIntensity = 0.8;
sandNoiseIntensity = 0.7;
mossNoiseIntensity = 0.2;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif