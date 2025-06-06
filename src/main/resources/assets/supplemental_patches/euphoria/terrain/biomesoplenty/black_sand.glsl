if (color.g - color.r > 0.06) {
    smoothnessG = pow(5.0 * color.r, 8);
    smoothnessG = min(smoothnessG, 0.05);
    smoothnessD = smoothnessG;

    highlightMult = 2.5;
} else {
    smoothnessG = pow2(color.g);
}

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif

#if RAIN_PUDDLES >= 1 || defined SPOOKY_RAIN_PUDDLE_OVERRIDE
    noPuddles = 1.0;
#endif