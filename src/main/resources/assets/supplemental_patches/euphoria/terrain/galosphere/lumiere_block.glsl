#include "/lib/materials/specificMaterials/terrain/lumiereBlock.glsl"

if (mat % 4 == 2) {  // charged lumiere
    if (color.r > 0.6 && color.g > 0.6 && color.b > 0.4) {
        emission += 3.0 * pow2(color.r);
    }
}