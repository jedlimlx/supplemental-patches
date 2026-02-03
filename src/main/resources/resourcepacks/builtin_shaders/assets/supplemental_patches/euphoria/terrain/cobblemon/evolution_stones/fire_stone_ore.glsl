if (color.r > 0.6 && color.r - color.b > 0.1) {  // Fire Stone Part
    #include "/lib/materials/specificMaterials/terrain/evolutionStoneBlock.glsl"
    emission = 1.2 * pow2(color.r);

    overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
    color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
    emission *= GLOWING_ORE_MULT;
} else {  // Stone / Netherrack Part
    if (mat % 8 == 0) {
        #include "/lib/materials/specificMaterials/terrain/stone.glsl"
    } else if (mat % 8 == 2) {
        #include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
    } else {
        #include "/lib/materials/specificMaterials/terrain/netherrack.glsl"
    }
}