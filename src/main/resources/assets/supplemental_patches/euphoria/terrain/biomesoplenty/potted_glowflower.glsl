noSmoothLighting = true;

float NdotE = dot(normalM, eastVec);
if (abs(abs(NdotE) - 0.5) < 0.4) {
    subsurfaceMode = 1, noDirectionalShading = true, isFoliage = true;
    emission = 2.0 * pow2(5.0 * max0(color.r - 0.8));
    color.rgb = pow1_5(color.rgb);
}

sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0;