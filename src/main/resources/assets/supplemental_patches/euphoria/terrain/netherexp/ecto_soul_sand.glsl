smoothnessG = color.r * 0.4;
smoothnessD = color.r * 0.25;

if (color.b > color.r) {
    emission = pow1_5(max0(2.0 * color.b - color.r)) + 0.1;
    color.rgb = pow1_5(color.rgb);
}