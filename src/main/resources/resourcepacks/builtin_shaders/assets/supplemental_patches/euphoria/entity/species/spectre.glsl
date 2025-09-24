if (color.b > 0.6) {
    emission = 1.5 * pow2(color.b);
    color.rgb = pow1_5(color.rgb);
} else {
    smoothnessG = color.b + 0.4;
    smoothnessD = smoothnessG;
}