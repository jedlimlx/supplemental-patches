if (color.r > 0.8) {
    materialMask = OSIEBCA; // Intense Fresnel
    float factor = min1(pow2(color.g) * 1.38);
    float factor2 = pow2(factor);
    smoothnessG = 1.0 - 0.5 * factor;
    highlightMult = factor2 * 3.5;
    smoothnessD = pow1_5(color.g);

    #ifdef COATED_TEXTURES
        noiseFactor = 0.33;
    #endif
} else {
    smoothnessG = (1.0 - pow(color.g, 64.0) * 0.3) * 0.4;
    highlightMult = 2.0;

    smoothnessD = smoothnessG;
}
