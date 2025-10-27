if (color.r - color.b > 0.04) {
    #include "/lib/materials/specificMaterials/terrain/redstoneTorch.glsl"
    emission = max(0.8, emission);
    overlayNoiseIntensity = 0.7, overlayNoiseEmission = 0.2;
} else {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
}