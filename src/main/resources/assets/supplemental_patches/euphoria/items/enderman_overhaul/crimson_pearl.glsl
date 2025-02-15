if (color.r > 0.9) {
    emission = 3.0 * color.g;
    color.r *= 1.2;

    overlayNoiseIntensity = 0.5;
}

smoothnessG = color.r * 0.5;
smoothnessD = smoothnessG;
highlightMult = 2.0;