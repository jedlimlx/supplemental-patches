if (color.r > 1.6 * color.g) {
    float factor = smoothstep1(min1(color.r * 1.5));
    factor = factor > 0.12 ? factor : factor * 0.5;
    smoothnessG = factor;
    smoothnessD = factor;
} else {
    #include "/lib/materials/specificMaterials/terrain/goldBlock.glsl"
}