float blockRes = absMidCoordPos.x * atlasSize.x;

smoothnessG = pow2(color.r) * (0.15 + 0.4 * Noise3D(floor((playerPos + cameraPosition) * blockRes) / blockRes));
smoothnessD = smoothnessG;
highlightMult = 3.0;

#ifdef COATED_TEXTURES
    noiseFactor = 0.85;
#endif