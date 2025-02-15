if (color.r > 0.23) { // Trumpet Log:Clean Part
    #include "/lib/materials/specificMaterials/planks/trumpetPlanks.glsl"
} else { // Trumpet Log:Wood Part, Trumpet Wood
    smoothnessG = pow2(color.r) * 0.35;
    smoothnessD = smoothnessG;

    #ifdef COATED_TEXTURES
        noiseFactor = 0.77;
    #endif
}