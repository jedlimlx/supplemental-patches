// block.12616 = ancient_brazier [limestone]
vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
float maxColor = max(color.r, max(color.g, color.b));
float minColor = min(color.r, min(color.g, color.b));
if (color.b < 0.5 && color.r < 0.3) {
    smoothnessG = color.b + 0.2;
    smoothnessD = smoothnessG;

    overlayNoiseIntensity = 0.6;
} else if (fractPos.y > 0.24 || maxColor - minColor > 0.1) {
    noDirectionalShading = true;
    emission = 2.4;
    color.rgb *= pow1_5(color.rgb);

    overlayNoiseIntensity = 0.0;
} else {
    smoothnessG = 0.6 * pow3(color.g);
    smoothnessD = smoothnessG;
    overlayNoiseIntensity = 0.7;
}