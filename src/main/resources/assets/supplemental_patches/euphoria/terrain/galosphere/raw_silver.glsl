#include "/lib/materials/specificMaterials/terrain/rawSilverBlock.glsl"
#ifdef GLOWING_RAW_BLOCKS
    emission = 0.6 * (sqrt2(color.b) + 1.2 * pow1_5(color.b));

    overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
    #ifdef SITUATIONAL_ORES
        emission *= skyLightCheck;
        color.rgb = mix(color.rgb, color.rgb * pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT))), skyLightCheck);
    #else
        color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
    #endif

    emission *= GLOWING_ORE_MULT;
#endif