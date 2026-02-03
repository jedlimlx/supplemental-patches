if (color.b - color.g > 0.1) {  // Dusk Stone Part
    #include "/lib/materials/specificMaterials/terrain/evolutionStoneBlock.glsl"

    #ifdef GLOWING_ORE_DIAMOND
        emission = 1.8 * pow2(color.b) + 0.3;

        overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
        #ifdef SITUATIONAL_ORES
            emission *= skyLightCheck;
            color.rgb = mix(color.rgb, color.rgb * pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT))), skyLightCheck);
        #else
            color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
        #endif
        emission *= GLOWING_ORE_MULT;
    #endif
} else {  // Stone Part
    if (mat % 4 == 0) {
        #include "/lib/materials/specificMaterials/terrain/stone.glsl"
    } else {
        #include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
    }
}