#ifdef CONNECTED_GLASS_EFFECT
    bool isPane = mat % 4 == 2;
    DoConnectedGlass(colorP, color, noGeneratedNormals, playerPos, worldGeoNormal, voxelNumber, isPane);
#endif

color.rgb *= vec3(1.0, 0.8, 0.5);

smoothnessG = 0.3;
highlightMult = 1.0;
reflectMult = 0.7;

overlayNoiseAlpha = 1.05;
mossNoiseIntensity = 0.8;
sandNoiseIntensity = 0.8;