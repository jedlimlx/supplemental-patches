float dotColor = dot(color.rgb, color.rgb);
if (color.g - color.r > 0.1 || dotColor > 2.9) {
    noDirectionalShading = true;
    emission = 2.1;
    color.rgb *= sqrt1(GetLuminance(color.rgb));

    overlayNoiseIntensity = 0.0;
} else {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
}