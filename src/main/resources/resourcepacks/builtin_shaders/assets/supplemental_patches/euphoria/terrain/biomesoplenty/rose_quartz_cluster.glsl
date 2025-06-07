materialMask = OSIEBCA; // Intense Fresnel

float factor = pow2(color.b);
smoothnessG = 1.0 - factor * 0.4;
highlightMult = factor * 3.0;
smoothnessD = factor;

noSmoothLighting = true;
lmCoordM.x *= 0.88;

#if defined GBUFFERS_TERRAIN
    vec3 worldPos = playerPos.xyz + cameraPosition.xyz;
    vec3 blockPos = abs(fract(worldPos) - vec3(0.5));
    float maxBlockPos = max(blockPos.x, max(blockPos.y, blockPos.z));
    emission = pow2(max0(1.0 - maxBlockPos * 1.85) * color.b) * 7.0;
    color.b *= 1.0 - emission * 0.07;
    emission *= 1.3;

    overlayNoiseIntensity = 0.5;
#endif

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif