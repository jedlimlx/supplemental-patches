if (color.b > 0.95 && !CheckForColor(color.rgb, vec3(238, 235, 255))) {
    emission = 0.8 * (color.b - color.g) + 0.7;
    color.rgb = saturateColors(color.rgb, 1.5);
}