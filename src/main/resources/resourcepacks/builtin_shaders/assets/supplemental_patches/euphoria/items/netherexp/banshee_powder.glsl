if (color.b - color.r > 0.1 || CheckForColor(color.rgb, vec3(255))) {
    emission = 2.0 * sqrt1(color.b) + 1.5 * pow2(pow2(5 * max0(color.b - 0.9)));
    color.rgb *= pow(color.rgb, vec3(pow2(emission / 3.5)));
}