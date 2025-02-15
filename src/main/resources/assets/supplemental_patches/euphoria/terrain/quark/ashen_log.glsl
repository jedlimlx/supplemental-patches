if (color.r > 0.8 && color.b > 0.8 && color.g > 0.8) { // Ashen Log:Clean Part
    #include "/lib/materials/specificMaterials/planks/ashenPlanks.glsl"
} else { // Ashen Log:Wood Part, Ashen Wood
    smoothnessG = pow2(color.r) * 0.1;
    smoothnessD = smoothnessG;

    #ifdef COATED_TEXTURES
        noiseFactor = 1.25;
    #endif
}