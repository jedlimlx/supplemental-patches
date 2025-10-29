noSmoothLighting = true;
lmCoordM.x = 0.77;

#include "/lib/materials/specificMaterials/planks/sprucePlanks.glsl"

if (color.b > 0.5) {
    emission = 2 * pow2(color.b) + 0.5;
    
    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 4.0, lViewPos);
    #endif
    
    #ifdef SNOWY_WORLD
        snowFactor = 0.0;
    #endif
    
    overlayNoiseIntensity = 0.3;
}