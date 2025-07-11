if (
    !CheckForColor(color.rgb, vec3(165, 173, 94)) &&
    !CheckForColor(color.rgb, vec3(194, 192, 110)) &&
    !CheckForColor(color.rgb, vec3(139, 153, 77)) &&
    !CheckForColor(color.rgb, vec3(71, 105, 70)) &&
    !CheckForColor(color.rgb, vec3(108, 128, 79)) &&
    !CheckForColor(color.rgb, vec3(60, 74, 57))
) {
    emission = pow1_5(max(color.r, max(color.g, color.b)));
}