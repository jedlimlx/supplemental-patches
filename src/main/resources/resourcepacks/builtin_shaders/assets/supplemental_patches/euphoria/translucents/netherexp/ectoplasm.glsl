lmCoordM = vec2(0.0);

emission = smoothstep1(sqrt1(color.b)) + 0.2;
emission *= 2.0;

smoothnessG = 0.3;
reflectMult = 0.3;

color.a *= 0.9;
color.rgb *= pow(color.rgb, vec3(0.5 + 0.3 * emission));

sandNoiseIntensity = 0.0, mossNoiseIntensity = 0.0;

#if ECTOPLASM_EDGE_EFFECT > 0
	float easeAmount = 1.5;
	vec3 edgeColor = vec3(0.15, 0.6, 0.7) * 3.25;
	float edgeEmission = 0.0;
	#if ECTOPLASM_EDGE_EFFECT == 2
		edgeColor *= 0.08;
		easeAmount = 1.2;
	#endif

	#include "/lib/materials/specificMaterials/terrain/fluidEdgeEffect.glsl"
#endif
