#include "/lib/materials/specificMaterials/terrain/evolutionStoneBlock.glsl"

emission = pow2(color.r) + 0.3;
overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
emission *= GLOWING_ORE_MULT;