if (color.r > 0.9) {
    emission = color.r > 0.95 ? 1.0 : 0.7 * pow2(color.r);
    color.rgb *= color.rgb;
}