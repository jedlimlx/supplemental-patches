vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
if ((fractPos.y > -0.24 && fractPos.y < 0.45) || color.b - color.r > 0.05) {
    noDirectionalShading = true;
    emission = 2.5;
    color.rgb *= pow1_5(color.rgb);

    overlayNoiseIntensity = 0.0;
} else {
    smoothnessG = pow2(color.g) * 0.4;
    smoothnessD = smoothnessG;
    overlayNoiseIntensity = 0.7;
}