if (color.b > 0.9) {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
} else {
    smoothnessG = 0.5 * pow3(color.r);
    smoothnessD = smoothnessG;
}