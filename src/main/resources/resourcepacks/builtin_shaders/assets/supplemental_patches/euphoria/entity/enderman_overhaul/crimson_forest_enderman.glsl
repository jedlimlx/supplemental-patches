if (color.r > 0.91) {
    emission = 3.0 * color.g;
    color.r *= 1.2;

    overlayNoiseIntensity = 0.5;
}

smoothnessG = color.r * 0.5;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif