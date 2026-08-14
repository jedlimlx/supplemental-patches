if (color.b > color.r || color.r > 2.0 * color.g) {
    #include "/lib/materials/specificMaterials/translucents/glass.glsl"
    color.a = pow(color.a, 1.0 - fresnel * 0.8);

    overlayNoiseAlpha = 0.8;
    sandNoiseIntensity = 0.8;
    mossNoiseIntensity = 0.8;

    if (mat % 4 == 2) {
        emission = pow1_5(color.b);
    }
} else {
    smoothnessG = 1.2 * smoothstep1(min1(1.3 * max0(color.r - 0.18)));
}
