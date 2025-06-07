if (mat % 4 == 1) {
    subsurfaceMode = 1, noSmoothLighting = true, noDirectionalShading = true;
    sandNoiseIntensity = 0.8, mossNoiseIntensity = 0.0, isFoliage = true;
} else {
    if (color.r > 0.8) {
        emission = 1.3 * pow2(color.r);
        color.rgb *= pow(color.rgb, vec3(0.3));
    } else lmCoordM.x *= 0.92;
}