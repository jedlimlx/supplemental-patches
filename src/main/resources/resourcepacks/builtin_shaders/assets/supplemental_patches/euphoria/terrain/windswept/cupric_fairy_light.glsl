noSmoothLighting = true;
lmCoordM.x = 0.77;

#include "/lib/materials/specificMaterials/planks/sprucePlanks.glsl"

if (color.g > 0.65) {
    emission = 2 * pow2(color.g) + 0.5;
    
    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
    #endif
    
    #ifdef SNOWY_WORLD
        snowFactor = 0.0;
    #endif
    
    overlayNoiseIntensity = 0.3;
}