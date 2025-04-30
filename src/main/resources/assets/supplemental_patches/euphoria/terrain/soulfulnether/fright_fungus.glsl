noSmoothLighting = true;

if (color.b / color.r > 1.6) {
    emission = 2.0 * (0.7 + 1.2 * signMidCoordPos.y);
    maRecolor = vec3(0.1);
}

sandNoiseIntensity = 0.3, mossNoiseIntensity = 0.0;
isFoliage = false;