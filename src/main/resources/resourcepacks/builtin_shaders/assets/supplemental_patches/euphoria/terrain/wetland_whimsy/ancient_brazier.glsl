vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
float maxColor = max(color.r, max(color.g, color.b));
float minColor = min(color.r, min(color.g, color.b));
if (color.b < 0.5 && color.r < 0.3 && color.g < 0.8) {
    smoothnessG = color.b + 0.2;
    smoothnessD = smoothnessG;

    overlayNoiseIntensity = 0.6;
} else if ((fractPos.y > 0.24 && fractPos.y < 0.45) || maxColor - minColor > 0.1) {
    noDirectionalShading = true;
    emission = 2.9;
    color.rgb *= pow1_5(color.rgb);

    overlayNoiseIntensity = 0.0;
} else {
    smoothnessG = pow2(color.g) * 0.4;
    smoothnessD = smoothnessG;
    overlayNoiseIntensity = 0.7;
}