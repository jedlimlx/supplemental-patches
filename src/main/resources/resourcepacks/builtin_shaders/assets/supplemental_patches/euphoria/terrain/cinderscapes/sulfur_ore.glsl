if (color.r / color.g < 2.0) {
    #include "/lib/materials/specificMaterials/terrain/sulfurBlock.glsl"

    #ifdef GLOWING_ORE_SULFUR
        emission = 1.4 * color.r * color.g + 0.1;

        overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
        #ifdef SITUATIONAL_ORES
            emission *= skyLightCheck;
            color.rgb = mix(color.rgb, color.rgb * pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT))), skyLightCheck);
        #else
            color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
        #endif

        emission *= GLOWING_ORE_MULT;
    #endif
} else {
    #include "/lib/materials/specificMaterials/terrain/netherrack.glsl"
}