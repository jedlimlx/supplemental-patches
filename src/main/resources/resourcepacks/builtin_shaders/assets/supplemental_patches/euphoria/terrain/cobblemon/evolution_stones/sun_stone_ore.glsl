if ((mat % 8 == 4 && color.r > 0.63) || (mat % 8 < 4 && color.r - color.b > 0.1)) {  // Fire Stone Part
    #include "/lib/materials/specificMaterials/terrain/evolutionStoneBlock.glsl"
    emission = pow2(color.r) + 0.3;

    overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
    color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
    emission *= GLOWING_ORE_MULT;
} else {  // Stone / Terracotta Part
    if (mat % 8 == 0) {
        #include "/lib/materials/specificMaterials/terrain/stone.glsl"
    } else if (mat % 8 == 2) {
        #include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
    } else {
        smoothnessG = 0.25;
        highlightMult = 1.5;
        smoothnessD = 0.17;

        #ifdef COATED_TEXTURES
            noiseFactor = 0.33;
        #endif
    }
}