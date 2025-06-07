if (
    (
        CheckForColor(color.rgb, vec3(38, 201, 190)) ||
        CheckForColor(color.rgb, vec3(138, 255, 255)) ||
        CheckForColor(color.rgb, vec3(36, 246, 216))
    ) && NdotU < 0.1
) {
    emission = 4.5;
} else if (color.r / color.b < 0.6 || color.b > 0.9) {  // Allurite Part
    emission = 2.5 * pow2(color.b) * sqrt(color.r);
    color.rgb *= sqrt1(GetLuminance(color.rgb));
    smoothnessD = 0;
    smoothnessG = 0;

} else {  // Silver Part
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
}