if (abs(color.b - color.r) > 0.04) {  // Raw Lead Part
    #include "/lib/materials/specificMaterials/terrain/rawLeadBlock.glsl"

    #ifdef GLOWING_ORE_LEAD
        emission = 3.0 * sqrt2(max(color.r, color.b));

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