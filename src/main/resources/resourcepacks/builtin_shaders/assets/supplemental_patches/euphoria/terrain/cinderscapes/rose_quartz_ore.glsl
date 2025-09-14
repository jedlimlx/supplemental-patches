if (color.r > 0.78) {
    #include "/lib/materials/specificMaterials/terrain/roseQuartzBlock.glsl"

    #ifdef GLOWING_ORE_ROSE_QUARTZ
        emission = 4.0 * pow2(color.g);

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