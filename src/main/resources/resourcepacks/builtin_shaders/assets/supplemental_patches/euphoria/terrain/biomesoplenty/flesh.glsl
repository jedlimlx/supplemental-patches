smoothnessG = 1.2 * color.r * (0.8 - color.r);
smoothnessD = smoothnessG;

if (mat % 4 == 1) {
    subsurfaceMode = 1, isFoliage = true;
}