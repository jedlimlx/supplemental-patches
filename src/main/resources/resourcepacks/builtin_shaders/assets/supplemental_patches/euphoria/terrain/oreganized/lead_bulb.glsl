#include "/lib/materials/specificMaterials/terrain/leadBlock.glsl"
lmCoordM.x *= 0.85;

if (color.b > 0.4) {
    lmCoordM.x = 1.0;
    emission = (pow1_5(color.b) - 0.3 * color.b) * 2.0 + 1.0;
    color.rgb *= color.rgb;
    color.r *= 1.2;
}