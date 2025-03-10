if (color.r - color.g > 0.03) {  // Raw Spinel Part
    #include "/lib/materials/specificMaterials/terrain/spinelBlock.glsl"

    #ifdef GLOWING_ORE_SPINEL
        emission = 0.45 * (1.6 * sqrt2(color.r) + 2.2 * pow2(color.r));

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