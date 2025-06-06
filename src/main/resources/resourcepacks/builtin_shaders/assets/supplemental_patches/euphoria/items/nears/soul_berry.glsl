if (color.b - color.r > 0.1) {
    emission = 3.5 * (color.b + 0.4);
    color.rgb *= color.rgb;
}

if (color.r > 0.64 && color.r > color.b && color.r > color.g && mat % 4 == 1) {
    emission = color.r < 0.75 ? 2.5 : 6.0;
}
