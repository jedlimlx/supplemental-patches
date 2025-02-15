if (color.b / color.r > 1.2 && color.b > 0.4) {
    materialMask = OSIEBCA; // Intense Fresnel
    float factor = pow2(0.6 * color.b + 0.4 * color.g);
    smoothnessG = 0.8 - factor * 0.3;
    highlightMult = factor * 3.0;
    smoothnessD = factor;

    overlayNoiseIntensity = 0.5;
} else if (color.r / color.b > 1.2 && color.r > 0.4) {
    materialMask = OSIEBCA; // Intense Fresnel
    float factor = pow2(0.8 * color.g + 0.2 * color.r);
    smoothnessG = 0.8 - factor * 0.3;
    highlightMult = factor * 3.0;
    smoothnessD = factor;

    overlayNoiseIntensity = 0.5;
}