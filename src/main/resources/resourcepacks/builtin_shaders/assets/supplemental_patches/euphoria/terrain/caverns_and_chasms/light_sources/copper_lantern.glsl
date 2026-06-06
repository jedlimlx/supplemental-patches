noSmoothLighting = true;
lmCoordM.x = min(lmCoordM.x, 0.77); // consistency748523

#include "/lib/materials/specificMaterials/terrain/copperBlock.glsl"

if (color.g > 0.73) {
	emission = 2.5 * pow2(color.g) + 0.5;
	color.rgb *= color.rgb;
}

#ifdef DISTANT_LIGHT_BOKEH
	DoDistantLightBokehMaterial(emission, 3.0, lViewPos);
#endif

#ifdef SNOWY_WORLD
	snowFactor = 0.0;
#endif

overlayNoiseIntensity = 0.3;
