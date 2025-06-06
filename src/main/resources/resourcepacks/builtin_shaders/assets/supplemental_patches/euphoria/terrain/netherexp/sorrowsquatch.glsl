smoothnessG = color.g * 0.05;
smoothnessD = smoothnessG;

if (mat % 4 == 2 && color.b - color.r > 0.1) {
    emission = 2.1;
    color.rgb *= sqrt1(GetLuminance(color.rgb));
}
