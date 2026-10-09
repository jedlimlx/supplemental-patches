if (maxOf(color.rgb) - min(color.r, min(color.b, color.g)) > 0.05 || maxOf(color.rgb) > 0.7) {  // Crystal Core Part
    float factor = GetLuminance(color.rgb);
    materialMask = OSIEBCA; // Intense Fresnel
    highlightMult = factor * 3.0;
    color.rgb *= 0.7 + 0.3 * GetLuminance(color.rgb);

    smoothnessG = 0.8 - factor * 0.3;
    smoothnessD = factor;

    #ifdef GLOWING_DEEPSLATE_CRYSTAL_CORE
        emission = sqrt(dot(color.rgb, color.rgb)) + 0.1;

        overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
        #ifdef SITUATIONAL_ORES
            emission *= skyLightCheck;
            color.rgb = mix(color.rgb, color.rgb * pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT))), skyLightCheck);
        #else
            color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
        #endif
        emission *= GLOWING_ORE_MULT;
    #endif
} else {  // Deepslate Part
    #include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
}
