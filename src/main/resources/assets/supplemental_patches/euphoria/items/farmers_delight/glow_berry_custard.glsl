lmCoordM.x *= 0.875;
if (color.g > 0.5 && color.r < 0.4) {
    emission = 0.5;
} else {
    emission = color.r < 0.75 ? 1.0 : 3.0;
    color.rgb = color.rgb * vec3(1.0, 0.8, 0.6);
}