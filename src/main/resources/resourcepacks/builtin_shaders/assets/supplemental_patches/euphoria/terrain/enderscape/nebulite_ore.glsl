if (color.r > color.g * 1.5) {  // Raw Nebulite Part
    #include "/lib/materials/specificMaterials/terrain/nebuliteBlock.glsl"

    #ifdef GLOWING_ORE_NEBULITE
        emission = 2.0 * sqrt(color.r);

        overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
        #ifdef SITUATIONAL_ORES
            emission *= skyLightCheck;
            color.rgb = mix(color.rgb, color.rgb * pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT))), skyLightCheck);
        #else
            color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
        #endif

        emission *= GLOWING_ORE_MULT;
    #endif
} else {  // Endstone / Mirestone Part
    if (mat % 4 == 0) {
        #include "/lib/materials/specificMaterials/terrain/endStone.glsl"
    } else {
        #include "/lib/materials/specificMaterials/terrain/mirestone.glsl"
    }
}