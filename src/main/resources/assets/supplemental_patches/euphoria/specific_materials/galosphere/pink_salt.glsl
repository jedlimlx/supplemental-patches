float blockRes = absMidCoordPos.x * atlasSize.x;

smoothnessG = pow2(color.r) * (0.05 + 0.5 * Noise3D(floor((playerPos + cameraPosition) * blockRes) / blockRes));
smoothnessD = smoothnessG;
highlightMult = 3.0;

#ifdef COATED_TEXTURES
    noiseFactor = 1.20;
#endif