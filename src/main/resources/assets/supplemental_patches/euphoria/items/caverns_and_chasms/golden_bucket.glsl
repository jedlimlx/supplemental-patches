if (color.r - color.b > 0.3 || CheckForColor(color.rgb, vec3(255))) {
    #include "/lib/materials/specificMaterials/terrain/goldBlock.glsl"
} else {
    float factor = color.b;
    smoothnessG = factor;
    highlightMult = factor * 2.0;
    smoothnessD = factor;
}