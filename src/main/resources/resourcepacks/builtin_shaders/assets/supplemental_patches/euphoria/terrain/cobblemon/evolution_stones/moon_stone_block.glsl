#include "/lib/materials/specificMaterials/terrain/evolutionStoneBlock.glsl"
#ifdef GLOWING_RAW_BLOCKS
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