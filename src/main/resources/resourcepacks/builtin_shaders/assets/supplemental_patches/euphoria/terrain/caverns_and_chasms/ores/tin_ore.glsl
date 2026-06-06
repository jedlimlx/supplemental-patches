if (color.r - color.g > 0.03) {  // Raw Tin Part
	#include "/lib/materials/specificMaterials/terrain/spinelBlock.glsl"

	#ifdef GLOWING_ORE_SPINEL
	emission = 1.2 * color.r + 0.1;

		overlayNoiseIntensity = 0.6, overlayNoiseEmission = 0.5;
		#ifdef SITUATIONAL_ORES
			emission *= skyLightCheck;
			color.rgb = mix(color.rgb, color.rgb * pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT))), skyLightCheck);
		#else
			color.rgb *= pow(color.rgb, vec3(0.5 * min1(GLOWING_ORE_MULT)));
		#endif
		emission *= GLOWING_ORE_MULT;
	#endif
} else {  // Stone Part
	if (mat % 8 == 0) {
		#include "/lib/materials/specificMaterials/terrain/stone.glsl"
	} else if (mat % 8 == 2) {
		#include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
	} else if (mat % 8 == 4) {
		#include "/lib/materials/specificMaterials/terrain/cylindrite.glsl"
	} else if (mat % 8 == 6) {
		#include "/lib/materials/specificMaterials/terrain/cassiterite.glsl"
	}
}
