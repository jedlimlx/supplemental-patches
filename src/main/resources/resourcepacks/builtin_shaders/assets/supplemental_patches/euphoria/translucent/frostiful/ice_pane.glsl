#ifdef CONNECTED_GLASS_EFFECT
    DoConnectedGlass(colorP, color, noGeneratedNormals, playerPos, worldGeoNormal, voxelNumber, true);
#endif

noSmoothLighting = true;

smoothnessG = pow2(color.g) * color.g;
highlightMult = pow2(min1(pow2(color.g) * 1.5)) * 3.5;
reflectMult = 0.7;
overlayNoiseAlpha = 0.6;
sandNoiseIntensity = 0.7;
mossNoiseIntensity = 0.7;