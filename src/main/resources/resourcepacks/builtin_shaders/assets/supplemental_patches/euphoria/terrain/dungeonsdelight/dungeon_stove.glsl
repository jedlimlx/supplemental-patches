float dotColor = dot(color.rgb, color.rgb);
if (color.r > color.g * 8 || (color.b > color.r * 1.5 && color.b > color.g)) {
    #include "/lib/materials/specificMaterials/terrain/stainedScrap.glsl"
} else if (color.g > 0.65 || (color.g > 2 * color.r)) {
    noDirectionalShading = true;
    emission = 2.50;
    color.rgb *= pow(GetLuminance(color.rgb), 0.4);

    overlayNoiseIntensity = 0.0;
} else {
    smoothnessG = pow2(color.g);
    smoothnessD = smoothnessG;

    #ifdef COATED_TEXTURES
        noiseFactor = 0.77;
    #endif
}