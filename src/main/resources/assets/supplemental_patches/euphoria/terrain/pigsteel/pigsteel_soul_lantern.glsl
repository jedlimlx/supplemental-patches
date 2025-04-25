noSmoothLighting = true;
lmCoordM.x = min(lmCoordM.x, 0.77); // consistency748523

#include "/lib/materials/specificMaterials/terrain/pigsteel.glsl"

if (color.b - color.r > 0.3 || color.b > 0.9) {
    emission = 1.45 * max0(color.g - color.r * 2.0);
    emission += 1.17 * min(pow2(pow2(0.55 * dot(color.rgb, color.rgb))), 3.5);
}

#ifdef DISTANT_LIGHT_BOKEH
    DoDistantLightBokehMaterial(color, vec4(0.5, 1.0, 1.0, 1.0), emission, 3.0, lViewPos);
#endif

#ifdef SNOWY_WORLD
    snowFactor = 0.0;
#endif

overlayNoiseIntensity = 0.3;