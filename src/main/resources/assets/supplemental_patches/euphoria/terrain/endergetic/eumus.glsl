if (mat % 4 < 2) {
    smoothnessG = color.r * 0.1 + 0.1;
    smoothnessD = smoothnessG;
    if (color.g > 0.5) {
        #include "/lib/materials/specificMaterials/terrain/endstone.glsl"
    }
} else {  // bricks
    float factor = 1.5 * smoothstep1(clamp01(color.r * 2.5));
    smoothnessG = pow2(factor);
    smoothnessD = smoothnessG;

    highlightMult = 1.5 * smoothnessG;
}