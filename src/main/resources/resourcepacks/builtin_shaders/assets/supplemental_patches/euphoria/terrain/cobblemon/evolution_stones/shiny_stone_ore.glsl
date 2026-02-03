if (color.r - color.b > 0.1 || color.r > 0.9) {  // Shiny Stone Part
    #include "/lib/materials/specificMaterials/terrain/evolutionStoneBlock.glsl"
    emission = 1.5 * pow3(pow3(color.r));

    overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
    color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
    emission *= GLOWING_ORE_MULT;
} else {  // Stone Part
    if (mat % 4 == 0) {
        #include "/lib/materials/specificMaterials/terrain/stone.glsl"
    } else {
        #include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
    }
}