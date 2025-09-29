if (color.b > 0.4) { // Diamond
    #include "/lib/materials/specificMaterials/terrain/diamondBlock.glsl"
    #ifdef GLOWING_ORE_DIAMOND
        emission = 0.8 * color.g + 1.5;

        overlayNoiseIntensity = 0.75, overlayNoiseEmission = 0.4;
        #ifdef SITUATIONAL_ORES
            emission *= skyLightCheck;
            color.rgb = mix(color.rgb, color.rgb * pow(color.rgb, vec3(min1(GLOWING_ORE_MULT))), skyLightCheck);
        #else
            color.rgb *= pow(color.rgb, vec3(min1(GLOWING_ORE_MULT)));
        #endif
        emission *= GLOWING_ORE_MULT;
    #endif
} else { // Obsidian
    #include "/lib/materials/specificMaterials/terrain/obsidian.glsl"
}