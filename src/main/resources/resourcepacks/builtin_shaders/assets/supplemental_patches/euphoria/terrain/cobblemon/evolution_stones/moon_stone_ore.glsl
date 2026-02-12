if ((mat % 8 == 4 && (color.b > color.r || color.g > color.r)) || (mat % 8 < 4 && maxOf(color.rgb) - minOf(color.rgb) > 0.05)) {  // Moon Stone Part
    #include "/lib/materials/specificMaterials/terrain/evolutionStoneBlock.glsl"

    #ifdef GLOWING_ORE_DIAMOND
        emission = 2.0 * pow(color.r * color.g * color.b, 0.1);

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
    if (mat % 8 == 0) {
        #include "/lib/materials/specificMaterials/terrain/stone.glsl"
    } else if (mat % 8 == 2) {
        #include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
    } else {
        smoothnessG = color.r * 0.35 + 0.2;
        smoothnessD = smoothnessG;

        #ifdef COATED_TEXTURES
            noiseFactor = 0.66;
        #endif
    }
}