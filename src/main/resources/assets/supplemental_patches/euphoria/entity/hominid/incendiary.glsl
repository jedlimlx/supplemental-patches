if (color.r > 0.95) {
    emission = dot(color.rgb, color.rgb) * 2.0;
    color.rgb *= color.rgb;
}