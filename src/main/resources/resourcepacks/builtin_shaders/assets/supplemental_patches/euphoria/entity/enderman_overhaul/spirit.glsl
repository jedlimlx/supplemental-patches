if (color.b > 1.3 * color.r || color.b > 0.9) {
    emission = 1.5;
    color.rgb = pow1_5(color.rgb);

    overlayNoiseIntensity = 0.0;
    color.a = pow1_5(color.b) - 0.05;
}