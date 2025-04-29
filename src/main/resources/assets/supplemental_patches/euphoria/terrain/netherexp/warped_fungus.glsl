noSmoothLighting = true;

#ifdef MOD_NETHEREXP
    if (color.r > 0.73 && color.b > 0.73) {
        emission = 1.5 * color.b;

        overlayNoiseIntensity = 0.5;
    }
#endif

if (color.r > 0.91) {
    emission = 3.0 * color.g;
    color.r *= 1.2;
    maRecolor = vec3(0.1);

    overlayNoiseIntensity = 0.5;
}

sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0;
isFoliage = false;