noSmoothLighting = true;
lmCoordM.x = 0.77;

#include "/lib/materials/specificMaterials/planks/sprucePlanks.glsl"

if (color.b > 0.5) {
    emission = max0(color.g - color.r * 2.0);
    emission += 1.2 * min(pow2(pow2(0.55 * dot(color.rgb, color.rgb))), 3.5);

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(color, vec4(0.5, 1.0, 1.0, 1.0), emission, 2.0, lViewPos);
    #endif

    #ifdef SNOWY_WORLD
        snowFactor = 0.0;
    #endif

    overlayNoiseIntensity = 0.3;
}