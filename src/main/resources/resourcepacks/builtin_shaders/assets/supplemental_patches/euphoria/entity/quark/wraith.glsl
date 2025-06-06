if (color.b > 0.7) {
    emission = 0.2 + 1.5 * pow2(color.b - color.r);
    color.rgb = pow1_5(color.rgb);
}