if (color.b > 0.3) { // Bald Cypress Log:Clean Part
    #include "/lib/materials/specificMaterials/planks/baldCypressPlanks.glsl"
} else { // Bald Cypress Log:Wood Part, Bald Cypress Wood
    smoothnessG = 0.4 * pow2(color.b);
    smoothnessD = smoothnessG;

    mossNoiseIntensity = 0.60;

    #ifdef COATED_TEXTURES
        noiseFactor = 0.77;
    #endif
}