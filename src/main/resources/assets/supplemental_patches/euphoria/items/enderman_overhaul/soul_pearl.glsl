smoothnessG = 1.0;
highlightMult = 2.0;
smoothnessD = 1.0;

if (color.b > 0.6) {
    emission = 1.5;
    color.rgb = pow1_5(color.rgb);
}