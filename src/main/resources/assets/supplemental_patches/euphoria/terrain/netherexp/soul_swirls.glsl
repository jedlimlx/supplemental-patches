subsurfaceMode = 1, isFoliage = true;

if (color.b > color.r) {
    float dotColor = dot(color.rgb, color.rgb);
    emission = 0.7 * dotColor;
}

sandNoiseIntensity = 0.0, mossNoiseIntensity = 0.0;