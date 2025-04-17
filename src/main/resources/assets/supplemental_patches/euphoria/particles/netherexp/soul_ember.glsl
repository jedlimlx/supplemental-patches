if (color.a > 0.0) {
    emission = 2.0;
    color.rgb *= color.rgb;

    if (CheckForColor(color, vec3(255))) emission += 3.0;
}