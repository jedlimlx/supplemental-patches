materialMask = OSIEBCA; // Intense Fresnel

smoothnessG = 0.8 - factor * 0.3;
highlightMult = factor * 3.0;
smoothnessD = factor;

noSmoothLighting = true;
lmCoordM.x *= 0.88;

#if GLOWING_AMETHYST >= 1 && defined GBUFFERS_TERRAIN
    vec3 worldPos = playerPos.xyz + cameraPosition.xyz;
    vec3 blockPos = abs(fract(worldPos) - vec3(0.5, 0.7, 0.5));
    float maxBlockPos = max(blockPos.x, max(blockPos.y, blockPos.z));
    emission = pow2(max0(1.0 - pow1_5(maxBlockPos) * 2.60) * min(color.r, min(color.g, color.b))) * 7.0;
    if (color.r < color.b && color.r < color.g) {
        color.r *= 1.0 - emission * 0.07;
    } else if (color.g < color.r && color.g < color.b) {
        color.g *= 1.0 - emission * 0.07;
    } else {
        color.b *= 1.0 - emission * 0.07;
    }

    emission *= 1.3;

    overlayNoiseIntensity = 0.5;
#endif

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif