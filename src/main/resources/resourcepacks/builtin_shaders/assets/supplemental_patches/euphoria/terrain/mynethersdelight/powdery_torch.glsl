bool bloomed = true;
#include "/lib/materials/specificMaterials/terrain/powderyCane.glsl"

float temp = mat % 4 == 0 ? 1.0 : 1.5;
lmCoordM.x = 0.9 * min1(0.7 + 0.3 * pow2(temp - signMidCoordPos.y - 2.0 * absMidCoordPos.x));