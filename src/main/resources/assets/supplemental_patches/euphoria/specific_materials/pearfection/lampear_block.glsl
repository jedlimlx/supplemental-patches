if ((color.r > color.g && color.r > 0.5) || color.r > 0.9) {
    emission = pow1_5(color.r) * 2.3 + 0.2;
    color.rgb *= sqrt(color.rgb);

    smoothnessG = 0.1;
    smoothnessD = smoothnessG;
    highlightMult = 1.5;
}