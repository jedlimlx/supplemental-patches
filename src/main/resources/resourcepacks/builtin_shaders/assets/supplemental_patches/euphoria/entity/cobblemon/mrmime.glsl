if (CheckForColor(color.rgb, vec3(242, 255, 255)) || (color.b > 0.78 && color.r / color.b < 0.8)) {
    materialMask = OSIEBCA; // Intense Fresnel

    float factor = pow2(color.g);
    float factor2 = pow2(factor);
    smoothnessG = 1.0 - 0.5 * factor;
    highlightMult = factor2 * 3.5;
    smoothnessD = factor;

    #ifdef COATED_TEXTURES
        noiseFactor = 0.33;
    #endif
}