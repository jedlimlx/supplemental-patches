#if ANISOTROPIC_FILTER == 0
    color = texture2DLod(tex, texCoord, 0);
#endif

noSmoothLighting = true;
if (color.r > 0.1 && color.g + color.b < 0.1) { // Redstone Parts
    noSmoothLighting = true; noDirectionalShading = true;
    lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

    if (color.r > 0.5) {
        color.rgb *= color.rgb;
        emission = 8.0 * color.r;

        overlayNoiseIntensity = 0.35, overlayNoiseEmission = 0.2;
    } else if (color.r > color.g * 2.0) {
        materialMask = OSIEBCA * 5.0; // Redstone Fresnel

        float factor = pow2(color.r);
        smoothnessG = 0.4;
        highlightMult = factor + 0.4;

        smoothnessD = factor * 0.7 + 0.3;
    }
} else if (color.b > color.r) {
    #include "/lib/materials/specificMaterials/terrain/silverBlock.glsl"
} else if (abs(color.r - color.b) < 0.15) { // Iron Parts
    #include "/lib/materials/specificMaterials/terrain/ironBlock.glsl"
} else if (color.g > color.b * 2.0) { // Gold Parts
    #include "/lib/materials/specificMaterials/terrain/goldBlock.glsl"
} else { // Wood Parts
    #include "/lib/materials/specificMaterials/planks/oakPlanks.glsl"
}