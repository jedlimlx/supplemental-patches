#include "/lib/materials/specificMaterials/terrain/pinkSalt.glsl"

emission = 2.1 * pow2(pow2((color.r + color.b + color.g) / 3.0));

float distance = pow2(signMidCoordPos.x) + pow2(signMidCoordPos.y);
emission /= (1 + 1.2 * sqrt2(distance));
color.rgb *= (1 + 0.3 * color.rgb);

overlayNoiseIntensity = 0.7, overlayNoiseEmission = 0.6;