if (GetMaxColorDif(color.rgb) > 0.01) {
    float maxComponent = max(max(color.r, color.g), color.b);
    float minComponent = min(min(color.r, color.g), color.b);
    float saturation = (maxComponent - minComponent) / (1.0 - abs(maxComponent + minComponent - 1.0));

    emission = 3.5 * saturation;
}