smoothnessG = 1.0;
smoothnessD = smoothnessG;
if (
    CheckForColor(color.rgb, vec3(255, 243, 221)) ||
    CheckForColor(color.rgb, vec3(237, 214, 164)) ||
    CheckForColor(color.rgb, vec3(255, 255, 255)) ||
    CheckForColor(color.rgb, vec3(223, 194, 115)) ||
    CheckForColor(color.rgb, vec3(156, 205, 182)) ||
    CheckForColor(color.rgb, vec3(146, 189, 171)) ||
    CheckForColor(color.rgb, vec3(173, 191, 182)) ||
    CheckForColor(color.rgb, vec3(164, 218, 190))
) {
    vec2 flickerNoise = texture2D(noisetex, vec2(frameTimeCounter * 0.02)).rb;
    emission = emission = 1.2 + 4.0 * pow2(max0(color.b - color.r));

    // appearance of flickering
    emission *= mix(1.0, min1(max(flickerNoise.r, flickerNoise.g) * 1.7), pow2(8 * 0.1));
} else {
    emission = 0.6;
}