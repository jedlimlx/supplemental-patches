if (color.b - color.r > 0.1) {
    emission = 0.3 * pow2(color.b);

    if (CheckForColor(color.rgb, vec3(1.0)))
        emission += 2.0;
}