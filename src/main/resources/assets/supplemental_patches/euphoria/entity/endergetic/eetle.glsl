vec3 hsv = rgb2hsv(color.rgb);
if (CheckForColor(color.rgb, vec3(178, 148, 254))) {
    emission = 0.4;
} else if ((color.r > 0.3 && abs(hsv.r * 36 - 27) < 2) || CheckForColor(color.rgb, vec3(48, 48, 115))) {
    emission = clamp(3.5 * pow1_5(color.b) + 1.5, 0.0, 7.0);
    color.rgb *= color.rgb;
} else {
    smoothnessG = 0.2 + 0.5 * color.b;
    smoothnessD = smoothnessG;
    highlightMult = 1.5;
}