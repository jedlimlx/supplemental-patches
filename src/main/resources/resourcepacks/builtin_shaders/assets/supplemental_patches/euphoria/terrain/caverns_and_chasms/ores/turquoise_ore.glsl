if (maxOf(color.rgb) - minOf(color.rgb) > 0.04) {  // Raw Turquoise Part
	#include "/lib/materials/specificMaterials/terrain/turquoiseBlock.glsl"

	#ifdef GLOWING_ORE_TURQUOISE
		emission = maxOf(color.rgb) + 1.2;

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
	if (mat % 4 == 0) {
		#include "/lib/materials/specificMaterials/terrain/stone.glsl"
	} else {
		#include "/lib/materials/specificMaterials/terrain/deepslate.glsl"
	}
}
