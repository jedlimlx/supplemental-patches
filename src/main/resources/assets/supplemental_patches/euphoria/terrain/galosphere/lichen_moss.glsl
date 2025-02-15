if (mat % 4 == 2) lmCoordM.x -= 0.1;
if (color.r > 0.5 && mat % 2 == 0) {
    emission = 7.0 * pow2(pow2(color.r));
}

smoothnessG = 0.10;
smoothnessD = smoothnessG;

mossNoiseIntensity = 0.8;

if (mat % 2 == 1) {
    subsurfaceMode = 1, noSmoothLighting = true, noDirectionalShading = true;
    sandNoiseIntensity = 0.8, mossNoiseIntensity = 0.0, isFoliage = true;
}