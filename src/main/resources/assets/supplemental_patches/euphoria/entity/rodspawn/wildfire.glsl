if (
    CheckForColor(color.rgb, vec3(164, 0, 0)) ||
    CheckForColor(color.rgb, vec3(244, 12, 12)) ||
    CheckForColor(color.rgb, vec3(255, 110, 110))
) {
    emission = 5.0;
    color.rgb *= color.rgb;
} else if (color.r > 0.95 || color.r > 3.0 * color.b) {
    lmCoordM = vec2(0.9, 0.0);
    emission = min(color.r, 0.7) * 2.5;
}