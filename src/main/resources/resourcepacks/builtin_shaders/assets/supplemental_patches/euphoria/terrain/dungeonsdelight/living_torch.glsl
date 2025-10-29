noSmoothLighting = true; noDirectionalShading = true;
lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

if (color.g - color.r > 0.2 || color.g > 0.7) {
    emission = 3.0;
    color.rgb = pow1_5(color.rgb);
    color.r = min1(color.r + 0.1);

    overlayNoiseIntensity = 0.0;
} else if (color.r > color.g * 8 || (color.b > color.r * 1.5 && color.b > color.g)) {
    #include "/lib/materials/specificMaterials/terrain/stainedScrap.glsl"
}

emission += 0.0001; // No light reducing during noon

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(emission, 3.0, lViewPos);
#endif