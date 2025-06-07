bool isPane = mat % 4 == 2;
#ifdef CONNECTED_GLASS_EFFECT
    DoConnectedGlass(colorP, color, noGeneratedNormals, playerPos, worldGeoNormal, voxelNumber, isPane);
#endif

smoothnessG = 0.3 * pow2(color.r);
highlightMult = smoothnessG + 0.1;
reflectMult = 0.1;

overlayNoiseAlpha = 1.25;
sandNoiseIntensity = 0.8;
mossNoiseIntensity = 0.8;

noSmoothLighting = isPane;
