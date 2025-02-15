if (color.g < 0.8) { // Azalea Log:Clean Part
    #include "/lib/materials/specificMaterials/planks/azaleaPlanks.glsl"
} else { // Azalea Log:Wood Part, Azalea Wood
    smoothnessG = pow2(color.r) * 0.1;
    smoothnessD = smoothnessG;

    mossNoiseIntensity = 0.45;

    #ifdef COATED_TEXTURES
        noiseFactor = 0.77;
    #endif
}