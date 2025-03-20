subsurfaceMode = 1;

if (color.b - color.r > 0.1) {
    emission = 4.5 * (color.b + 0.4);
    color.rgb *= color.rgb;
    isFoliage = false;
} else {
    isFoliage = true;
}

sandNoiseIntensity = 0.0, mossNoiseIntensity = 0.0;