if (color.r > 3.0 * color.b || (color.r > 0.9 && abs(color.g - color.b) < 0.1)) {
    noDirectionalShading = true;
    emission = 4.00;
    color.rgb *= pow(GetLuminance(color.rgb), 0.3);

    overlayNoiseIntensity = 0.0;
} else {
    #include "/lib/materials/specificMaterials/terrain/oakWood.glsl"
}