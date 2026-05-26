noSmoothLighting = true; noDirectionalShading = true;
if (color.r > 0.74 && mat % 4 == 2) {  // molten heliostone
	emission = pow2(pow2(color.r)) * 4.0;

	#if RAIN_PUDDLES >= 1
		noPuddles = color.g * 4.0;
	#endif

	color.gb *= max(2.0 - 11.0 * pow2(color.g), 0.5);

	maRecolor = vec3(emission * 0.075);

	overlayNoiseIntensity = 0.0;
} else {  // normal heliostone
	#include "/lib/materials/specificMaterials/terrain/netherrack.glsl"
}
