if (color.r > 0.7 && color.g > 0.7 && color.b > 0.7) {
    smoothnessG = pow(color.g, 16.0) * 2.0;
    smoothnessD = smoothnessG * 0.7;
    highlightMult = 2.0;
} else {
    #include "/lib/materials/specificMaterials/terrain/sack.glsl"
}