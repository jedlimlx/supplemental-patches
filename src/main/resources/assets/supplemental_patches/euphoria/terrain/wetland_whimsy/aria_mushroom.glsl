if (mat % 4 == 0) {
    emission = 2.5 * pow2(color.r) + 0.2 * pow2(color.b);
    overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
} else {
    noSmoothLighting = true;

    vec3 worldPos = playerPos + cameraPosition;
    float fractPos = fract(worldPos.y);
    if (fractPos > 0.375) {
        emission = 2.5 * pow2(color.r) + 0.2 * pow2(color.b);
        overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
    }

    sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0;
}