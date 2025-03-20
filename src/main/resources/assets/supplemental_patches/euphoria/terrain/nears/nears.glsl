subsurfaceMode = 2, isFoliage = true;

if (color.b > color.r) {  // purple stem
    smoothnessG = 0.2 + 0.4 * color.b;
    smoothnessD = smoothnessG;
} else {  // near
    emission = color.r > 0.5 ? 3.0 * pow2(color.r) : 0.0;

    smoothnessG = 0.4;
    smoothnessD = smoothnessG;
}

#ifdef COATED_TEXTURES
    noiseFactor = 0.77;
#endif