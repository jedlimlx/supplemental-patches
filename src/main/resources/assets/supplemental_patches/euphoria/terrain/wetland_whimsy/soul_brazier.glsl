vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
if (fractPos.y > -0.24 || color.b - color.r > 0.05) {
    noDirectionalShading = true;
    emission = 2.5;
    color.rgb *= pow1_5(color.rgb);

    overlayNoiseIntensity = 0.0;
} else {
    smoothnessG = 0.6 * pow3(color.g);
    smoothnessD = smoothnessG;
    overlayNoiseIntensity = 0.7;
}