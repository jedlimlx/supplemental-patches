smoothnessG = pow2(color.r) * (0.2 + 0.7 * Noise3D(floor((playerPos + cameraPosition) * 16.0) / 16.0));
smoothnessD = smoothnessG;
highlightMult = 3.0;

#ifdef COATED_TEXTURES
    noiseFactor = 0.85;
#endif