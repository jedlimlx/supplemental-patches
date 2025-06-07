if (color.a > 0.8) {
    smoothnessG = color.r;
    reflectMult = 1.0;
    highlightMult = 1.0;
} else {
    #include "/lib/materials/specificMaterials/translucents/stainedGlass.glsl"
    overlayNoiseAlpha = 1.05;
    sandNoiseIntensity = 0.8;
    mossNoiseIntensity = 0.8;
}