float blockRes = 16;
vec3 pWorldPos = floor((playerPos + cameraPosition) * blockRes) / blockRes;

smoothnessG = pow2(color.r) * (0.05 + 0.5 * hash13(pWorldPos));
smoothnessD = smoothnessG;
highlightMult = 3.0;

#ifdef COATED_TEXTURES
    noiseFactor = 1.20;
#endif
