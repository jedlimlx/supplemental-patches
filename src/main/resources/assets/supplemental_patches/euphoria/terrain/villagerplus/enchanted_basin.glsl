if (color.g < 0.5 && color.r + color.b > 1.3) {  // Amethyst Part
    #include "/lib/materials/specificMaterials/terrain/amethystBlock.glsl"
} else if ((color.g + color.r) / color.b > 2.4 && NdotU > 0.9) {  // Experience Part
    highlightMult = 0.0;
    smoothnessD = 0.0;
    emission = pow1_5(color.g) * 6.0;
    maRecolor = vec3(0.0);

    color.rgb *= color.rgb;
    overlayNoiseIntensity = 0.0;
} else if (color.r + color.b + color.g > 2.0) {  // Calcite Part
    highlightMult = pow2(color.g) + 1.0;
    smoothnessG = 1.0 - color.g * 0.5;
    smoothnessD = smoothnessG;
}