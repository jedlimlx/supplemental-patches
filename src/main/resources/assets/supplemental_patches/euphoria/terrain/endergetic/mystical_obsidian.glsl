if (color.g > 0.4 && abs(color.r - color.g) < 0.16) {  // endstone
    #include "/lib/materials/specificMaterials/terrain/endstone.glsl"
} else {  // obsidian
    #include "/lib/materials/specificMaterials/terrain/obsidian.glsl"

    emission = color.r > 0.5 ? 2.7 * sqrt(color.r) : 0.0;

    overlayNoiseIntensity = 0.65;
    overlayNoiseEmission = 0.6;
}