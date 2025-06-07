float dotColor = dot(color.rgb, color.rgb);
if (CheckForColor(color.rgb, vec3(221, 54, 72)) || CheckForColor(color.rgb, vec3(137, 29, 52))) {
    emission = 4.0;
} else if (CheckForColor(color.rgb, vec3(255))) {
    emission = 5.0;
} else {
    float factor = smoothstep1(min1(color.r * 1.5));
    factor = factor > 0.12 ? factor : factor * 0.5;
    smoothnessG = factor;
    smoothnessD = factor;
}