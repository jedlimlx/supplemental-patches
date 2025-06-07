noSmoothLighting = true;
lmCoordM.x = min(lmCoordM.x, 0.77); // consistency748523

#include "/lib/materials/specificMaterials/terrain/lanternMetal.glsl"

if (color.b > 0.5) {
    emission = 2.5 * pow2(color.b) + 0.5;
}

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(emission, 3.0, lViewPos);
#endif

#ifdef SNOWY_WORLD
    snowFactor = 0.0;
#endif

overlayNoiseIntensity = 0.3;