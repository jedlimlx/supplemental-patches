if (mat % 8 == 0) {  // red
    if (color.r > 0.62 || CheckForColor(color.rgb, vec3(93, 14, 65))) {
        emission = 1.8 * pow2(color.r);
    }
} else if (mat % 8 == 2) {  // yellow
    if (color.r > 0.78) {
        emission = pow2(color.g) + 1.5 * color.b;
    }
} else if (mat % 8 == 4) {  // green
    if (color.g > 0.78 || CheckForColor(color.rgb, vec3(5, 146, 18))) {
        emission = 1.2 * pow2(color.g);
    }
} else if (mat % 8 == 6) {  // blue
    if (color.b > 0.98) {
        emission = 1.2 * pow2(color.g);
    }
}
