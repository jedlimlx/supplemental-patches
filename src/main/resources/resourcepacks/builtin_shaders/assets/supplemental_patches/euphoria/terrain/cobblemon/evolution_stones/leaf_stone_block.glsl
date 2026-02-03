#include "/lib/materials/specificMaterials/terrain/evolutionStoneBlock.glsl"
#ifdef GLOWING_RAW_BLOCKS
    emission = 1.5 * pow2(color.g) / color.r;

    overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
    #ifdef SITUATIONAL_ORES
        emission *= skyLightCheck;
        color.rgb = mix(color.rgb, color.rgb * pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT))), skyLightCheck);
    #else
        color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
    #endif
    emission *= GLOWING_ORE_MULT;
#endif