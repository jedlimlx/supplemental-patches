if (color.b > 0.65) {
    emission = 3.0;
    color.rgb *= sqrt1(GetLuminance(color.rgb));
}