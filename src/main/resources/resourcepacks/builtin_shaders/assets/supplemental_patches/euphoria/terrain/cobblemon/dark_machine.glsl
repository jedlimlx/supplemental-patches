if (color.b / color.r < 0.2) {
    noSmoothLighting = true; noDirectionalShading = true;
    lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

    materialMask = OSIEBCA * 5.0; // Redstone Fresnel

    float factor = pow2(color.r);
    smoothnessG = 0.4;
    highlightMult = factor + 0.4;

    smoothnessD = factor * 0.7 + 0.3;
} else if (CheckForColor(color.rgb, vec3(106, 190, 48)) || CheckForColor(color.rgb, vec3(255, 0, 0))) {
    emission = 2.0;
    color.rgb *= color.rgb;
} else {
    #include "/lib/materials/specificMaterials/terrain/anvil.glsl"
}