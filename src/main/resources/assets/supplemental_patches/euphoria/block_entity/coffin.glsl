if (color.r > 0.5 && blockEntityId % 4 != 1) {
    emission = 7.0;
    color.rgb *= color.rgb;
} else if (color.b - color.r > 0.05) {
    smoothnessG = color.b + 0.2;
    smoothnessD = smoothnessG;
} else {
    #include "/lib/materials/specificMaterials/terrain/cobblestone.glsl"
}