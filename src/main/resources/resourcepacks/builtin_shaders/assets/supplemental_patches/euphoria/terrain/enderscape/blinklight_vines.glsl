if (mat % 2 == 0 && ((color.r - color.g > 0.05 && color.b - color.r > 0.15) || color.b > 0.83)) {
    smoothnessG = 0.7;
    smoothnessD = smoothnessG;

    emission = 4.0 * pow2(pow2(min(color.r, color.b)));
    color.rgb *= color.rgb;
} else {
    subsurfaceMode = 1, noDirectionalShading = true, noSmoothLighting = true;
    isFoliage = true;
}