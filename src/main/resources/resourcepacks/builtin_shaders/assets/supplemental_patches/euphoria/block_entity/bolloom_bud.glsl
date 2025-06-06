if (color.g > 0.65 && color.g * 1.2 > color.r) {
    float dotColor = dot(color.rgb, color.rgb);
    emission = pow2(pow2(dotColor * 0.5)) + 0.6 * dotColor;
}