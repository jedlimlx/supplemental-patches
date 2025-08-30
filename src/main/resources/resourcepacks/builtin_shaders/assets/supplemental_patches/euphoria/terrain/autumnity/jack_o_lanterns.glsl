#include "/lib/materials/specificMaterials/terrain/pumpkin.glsl"
noSmoothLighting = true, noDirectionalShading = true;

if (color.g < 0.1 && mat % 8 == 0) {
    noSmoothLighting = true; noDirectionalShading = true;
    lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

    #include "/lib/materials/specificMaterials/terrain/redstoneTorch.glsl"
    emission += 0.0001; // No light reducing during noon

    lmCoordM.x += 0.1;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(color, vec4(1.0, 0.0, 0.0, 1.0), emission, 5.0, lViewPos);
    #endif

    overlayNoiseIntensity = 0.0;
} else if (color.b > 0.6 && mat % 8 == 2) {
    noSmoothLighting = true; noDirectionalShading = true;
    lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

    emission = 2.7;
    color.rgb = pow1_5(color.rgb);
    color.r = min1(color.r + 0.1);

    overlayNoiseIntensity = 0.0;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(color, vec4(0.5, 1.0, 1.0, 1.0), emission, 3.0, lViewPos);
    #endif
} else if (color.b > 0.95 && mat % 8 == 4) {
    noSmoothLighting = true; noDirectionalShading = true;
    lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);
    emission = 3.0;

    color.rgb *= color.rgb;

    overlayNoiseIntensity = 0.0;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 3.0, lViewPos);
    #endif
} else if (color.g > 0.7 && color.g - color.r > 0.2 && mat % 8 == 6) {
    noSmoothLighting = true; noDirectionalShading = true;
    lmCoordM.x = min(lmCoordM.x * 0.9, 0.77);

    emission = 3.0;
    color.rgb = pow2(color.rgb);
    color.r = min1(color.r + 0.1);

    overlayNoiseIntensity = 0.0;

    #ifdef DISTANT_LIGHT_BOKEH
        DoDistantLightBokehMaterial(emission, 5.0, lViewPos);
    #endif
} else {
    lmCoordM.x *= 0.88;
}