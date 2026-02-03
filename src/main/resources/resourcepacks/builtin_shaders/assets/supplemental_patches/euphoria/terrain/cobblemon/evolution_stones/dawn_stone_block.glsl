#include "/lib/materials/specificMaterials/terrain/evolutionStoneBlock.glsl"

emission = 1.2 * pow2(color.b);
overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
emission *= GLOWING_ORE_MULT;