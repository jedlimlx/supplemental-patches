if (color.b > color.r) {
    #include "/lib/materials/specificMaterials/translucents/glass.glsl"
    color.a = pow(color.a, 1.0 - fresnel * 0.8);

    overlayNoiseAlpha = 0.8;
    sandNoiseIntensity = 0.8;
    mossNoiseIntensity = 0.8;
} else {
    float factor = pow2(color.g) * 0.6;

    smoothnessG = factor;
    highlightMult = 1.0 + 2.5 * factor;
}