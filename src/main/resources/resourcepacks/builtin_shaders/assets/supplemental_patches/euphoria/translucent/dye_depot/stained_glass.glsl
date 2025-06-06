#ifdef CONNECTED_GLASS_EFFECT
    bool isPane = mat % 2 == 1;
    DoConnectedGlass(colorP, color, noGeneratedNormals, playerPos, worldGeoNormal, voxelNumber, isPane);
#endif

#include "/lib/materials/specificMaterials/translucents/stainedGlass.glsl"
overlayNoiseAlpha = 1.05;
sandNoiseIntensity = 0.8;
mossNoiseIntensity = 0.8;