if (color.b - color.r > 0.05 || color.b > 0.8) {  // Raw Silver Part
    #include "/lib/materials/specificMaterials/terrain/rawSilverBlock.glsl"

    #ifdef GLOWING_ORE_SILVER_O
        emission = 2.0 * pow1_5(color.b);

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
    #include "/lib/materials/specificMaterials/terrain/stone.glsl"
}