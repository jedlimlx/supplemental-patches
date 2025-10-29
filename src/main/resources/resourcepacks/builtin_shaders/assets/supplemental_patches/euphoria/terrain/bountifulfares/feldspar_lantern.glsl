noSmoothLighting = true;
lmCoordM.x = min(lmCoordM.x, 0.77); // consistency748523

#include "/lib/materials/specificMaterials/terrain/lanternMetal.glsl"

if (color.r > 0.38) {
    emission = 0.8 * pow2(color.r) + 1.0;
}

#ifdef SNOWY_WORLD
    snowFactor = 0.0;
#endif

overlayNoiseIntensity = 0.3;