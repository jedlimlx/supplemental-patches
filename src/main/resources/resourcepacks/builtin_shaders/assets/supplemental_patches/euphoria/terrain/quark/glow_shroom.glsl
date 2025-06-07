noSmoothLighting = true;

if (color.g > color.b + 0.035 && color.g > 0.72) {
    emission = 1.25 * pow2(pow2(color.g)) + 0.3 * pow2(color.b);
} else {
    sandNoiseIntensity = 0.0, mossNoiseIntensity = 0.0;
}
