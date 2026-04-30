if (color.r < 0.4) {
    materialMask = OSIEBCA; // Intense Fresnel
    #include "/lib/materials/specificMaterials/terrain/anvil.glsl"

    smoothnessG *= 1.5;
    smoothnessD *= 1.5;
}