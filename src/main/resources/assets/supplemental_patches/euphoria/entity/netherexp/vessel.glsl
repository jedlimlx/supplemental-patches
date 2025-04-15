if (color.b - color.r > 0.1) {
    emission = 3.0;
    color.rgb *= sqrt1(GetLuminance(color.rgb));
}