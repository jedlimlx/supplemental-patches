if (color.r > 0.8 || color.b > 0.8) {
    emission = 2.0;
    color.rgb *= sqrt1(GetLuminance(color.rgb));

    overlayNoiseIntensity = 0.0;
}