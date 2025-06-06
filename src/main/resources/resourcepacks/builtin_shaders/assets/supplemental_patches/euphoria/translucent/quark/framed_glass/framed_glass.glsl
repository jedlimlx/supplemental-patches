if (
    CheckForColor(color.rgb, vec3(57, 54, 52)) ||
    CheckForColor(color.rgb, vec3(65, 68, 69)) ||
    CheckForColor(color.rgb, vec3(71, 79, 82)) ||
    CheckForColor(color.rgb, vec3(44, 40, 37)) ||
    CheckForColor(color.rgb, vec3(64, 67, 68)) ||
    CheckForColor(color.rgb, vec3(43, 40, 37)) ||
    CheckForColor(color.rgb, vec3(33, 35, 31)) ||
    CheckForColor(color.rgb, vec3(55, 55, 55))
) {
    smoothnessG = color.r;
} else {
    #include "/lib/materials/specificMaterials/translucents/glass.glsl"

    overlayNoiseAlpha = 0.8;
    sandNoiseIntensity = 0.8;
    mossNoiseIntensity = 0.8;
}