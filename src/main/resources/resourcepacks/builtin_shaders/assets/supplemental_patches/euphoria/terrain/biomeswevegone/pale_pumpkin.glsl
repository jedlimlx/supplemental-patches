lmCoordM.x *= 0.77;

if (color.b - color.r > 0.15 || CheckForColor(color.rgb, vec3(255))) {
    emission = 2.0;
    color.rgb = pow1_5(color.rgb);

    overlayNoiseIntensity = 0.0;
} else {
    smoothnessG = 0.1;
}