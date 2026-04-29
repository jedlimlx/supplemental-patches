#if defined GBUFFERS_TERRAIN
	noSmoothLighting = true;

	vec2 pixelTexSize = ivec2(absMidCoordPos * 2.0 * atlasSize);
	if (mat % 2 == 0 && pixelTexSize.x == 1 && pixelTexSize.y == 4) {
		color.rgb *= 1.0 + 0.7 * pow2(max(-signMidCoordPos.y + 0.6, float(NdotU > 0.9) * 1.6));

		#ifdef SNOWY_WORLD
			snowFactor = 0.0;
		#endif

		overlayNoiseIntensity = 0.3;
	} else {
		if (color.r > 0.9) {
			#include "/lib/materials/specificMaterials/terrain/goldBlock.glsl"
		} else {
			#include "/lib/materials/specificMaterials/terrain/ironBlock.glsl"
		}
	}
#endif
