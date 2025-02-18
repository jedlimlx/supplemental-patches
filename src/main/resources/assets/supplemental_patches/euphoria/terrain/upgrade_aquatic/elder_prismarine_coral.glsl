smoothnessG = pow2(color.g) * 0.8;
highlightMult = 1.5;
smoothnessD = smoothnessG;

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif

if (mat % 4 < 2) {
    noSmoothLighting = true;

    #if defined GBUFFERS_TERRAIN
        vec3 worldPos = playerPos.xyz + cameraPosition.xyz;
        vec3 blockPos;
        if (mat % 4 == 0) {
            blockPos = abs(fract(worldPos) - vec3(0.5));
        } else {
            blockPos = abs(fract(worldPos) - vec3(0.5, 0.2, 0.5));
        }

        float r = length(blockPos);
        emission = smoothnessG - 0.5 * pow3(color.g);
        emission *= pow2(max0(1.0 - r * 1.3) * color.r) * 20.0;
        color.r *= 1.0 - emission * 0.05;

        overlayNoiseIntensity = 0.5;
    #endif
}