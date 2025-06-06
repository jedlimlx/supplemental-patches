emission = abs(color.r - color.b) * 3.0 + 0.1;
if (GetMaxColorDif(color.rgb) < 0.03) {
    emission = color.g > 0.2 ? 3.0 * color.g : 0.0;
}

color.rgb = pow(color.rgb, vec3(1.0 + 0.5 * sqrt(emission)));
