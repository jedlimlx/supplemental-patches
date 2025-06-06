smoothnessG = color.r * 0.4;
smoothnessD = color.r * 0.25;

if (color.b - color.r > 0.1) {
    float dotColor = dot(color.rgb, color.rgb);
    emission = 1.3 * dotColor;
    color.rgb *= color.rgb;
}