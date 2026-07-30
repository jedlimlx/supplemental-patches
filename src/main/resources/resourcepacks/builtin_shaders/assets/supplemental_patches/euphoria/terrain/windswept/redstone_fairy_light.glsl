noSmoothLighting = true;
lmCoordM.x = 0.77;

#include "/lib/materials/specificMaterials/planks/sprucePlanks.glsl"

if (color.r / color.b > 4 || (color.r > 0.5 && color.r / color.b > 2) || color.r > 0.9) {
	materialMask = OSIEBCA * 5.0; // Redstone Fresnel

	float factor = pow2(color.r);
	smoothnessG = 0.4;
	highlightMult = factor + 0.4;

	smoothnessD = factor * 0.7 + 0.3;

	emission = max(3.5 - 2.25 * color.g, 0.3);
	color.rgb *= color.rgb;

	#ifdef DISTANT_LIGHT_BOKEH
		DoDistantLightBokehMaterial(color, vec4(1.0, 0.0, 0.0, 1.0), emission, 4.0, lViewPos);
	#endif

	#ifdef SNOWY_WORLD
		snowFactor = 0.0;
	#endif

	overlayNoiseIntensity = 0.3;
}
