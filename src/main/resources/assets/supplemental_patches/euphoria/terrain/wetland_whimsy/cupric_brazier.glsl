vec3 fractPos = fract(playerPos + cameraPosition) - 0.5;
#ifdef GBUFFERS_TERRAIN
    lmCoordM.x *= 0.88;
    if (NdotU > 0.9 && length(fractPos) < 0.8) {
        lmCoordM.x += 0.8 * pow2(0.8 - length(fractPos));
    }
#endif

if ((fractPos.y > -0.24 && fractPos.y < 0.45) || (color.r > 0.6 && color.g > color.r)) {
    noDirectionalShading = true;
    emission = 2.50;
    color.rgb *= sqrt1(GetLuminance(color.rgb));

    overlayNoiseIntensity = 0.0;
} else {
    smoothnessG = pow2(color.g) * 0.4;
    smoothnessD = smoothnessG;
    overlayNoiseIntensity = 0.7;
}