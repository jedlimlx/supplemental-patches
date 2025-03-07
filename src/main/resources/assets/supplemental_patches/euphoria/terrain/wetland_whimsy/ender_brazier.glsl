vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
#ifdef GBUFFERS_TERRAIN
    lmCoordM.x *= 0.88;
    if (NdotU > 0.9 && length(fractPos) < 0.8) {
        lmCoordM.x += 0.8 * pow2(0.8 - length(fractPos));
    }
#endif

float dotColor = dot(color.rgb, color.rgb);
if ((fractPos.y > -0.24 && fractPos.y < 0.45) || (color.r - color.g > 0.1 && color.b - color.g > 0.1)) {
    noDirectionalShading = true;
    emission = 3.50;
    color.rgb *= color.rgb;
} else {
    smoothnessG = pow2(color.g) * 0.4;
    smoothnessD = smoothnessG;
    overlayNoiseIntensity = 0.7;
}