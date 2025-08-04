noSmoothLighting = true;

float NdotE = dot(normalM, eastVec);
if (abs(abs(NdotE) - 0.5) < 0.4) {
    subsurfaceMode = 1, noDirectionalShading = true, isFoliage = true;
    emission = color.r > 0.78 ? 0.8 * pow2(pow2(color.r)) : 0.0;
}