noSmoothLighting = true;
lmCoordM.x = min(lmCoordM.x, 0.77); // consistency748523

if (
    CheckForColor(color.rgb, vec3(215, 111, 75)) ||
    CheckForColor(color.rgb, vec3(212, 109, 73)) ||
    CheckForColor(color.rgb, vec3(198, 98, 63)) ||
    CheckForColor(color.rgb, vec3(174, 115, 75)) ||
    CheckForColor(color.rgb, vec3(166, 103, 61)) ||
    CheckForColor(color.rgb, vec3(166, 68, 34)) ||
    CheckForColor(color.rgb, vec3(132, 70, 27))
) {
    #include "/lib/materials/specificMaterials/terrain/copperBlock.glsl"
} else if (
    !CheckForColor(color.rgb, vec3(93, 143, 194)) &&
    !CheckForColor(color.rgb, vec3(179, 207, 236)) &&
    !CheckForColor(color.rgb, vec3(212, 229, 247)) &&
    (color.r / color.g > 1.2 || color.r / color.g < 1.15)
) {
    float dotColor = dot(color.rgb, color.rgb);
    emission = 0.3 * dotColor + 1.4;

    overlayNoiseIntensity = 0.3;
}
