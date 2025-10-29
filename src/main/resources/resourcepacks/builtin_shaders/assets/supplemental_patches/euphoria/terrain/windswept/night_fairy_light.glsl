noSmoothLighting = true;
lmCoordM.x = 0.77;

#include "/lib/materials/specificMaterials/planks/sprucePlanks.glsl"

if (color.b > color.r || color.b > 0.6) {
    emission = 2 * pow2(color.b) + 0.7;
    color.rg *= color.rg;
    maRecolor = vec3(0.1);
    
    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 2.0, lViewPos);
    #endif
    
    #ifdef SNOWY_WORLD
        snowFactor = 0.0;
    #endif
    
    overlayNoiseIntensity = 0.3;
}