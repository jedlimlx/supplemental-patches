noSmoothLighting = true;
lmCoordM.x = 0.77;

#include "/lib/materials/specificMaterials/planks/sprucePlanks.glsl"

if (color.r / color.b > 4 || color.r > 0.5) {
    #include "/lib/materials/specificMaterials/terrain/redstoneTorch.glsl"
    emission *= 1.2;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(color, vec4(1.0, 0.0, 0.0, 1.0), emission, 4.0, lViewPos);
    #endif
    
    #ifdef SNOWY_WORLD
        snowFactor = 0.0;
    #endif
    
    overlayNoiseIntensity = 0.3;
}