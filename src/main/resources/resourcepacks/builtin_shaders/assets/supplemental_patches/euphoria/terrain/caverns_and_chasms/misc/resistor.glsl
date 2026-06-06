noSmoothLighting = true;

#if ANISOTROPIC_FILTER > 0 && !defined IPBR_COMPAT_MODE
	color = texture2D(tex, texCoord); // Fixes artifacts
	color.rgb *= glColor.rgb;
#endif

vec3 absDif = abs(vec3(color.r - color.g, color.g - color.b, color.r - color.b));
float maxDif = max(absDif.r, max(absDif.g, absDif.b));
if (color.r > 0.68 && color.g < 0.05 && color.b < 0.05) { // Redstone Parts
	color.gb *= 0.5; // Comparator:Emissive Wire
	#include "/lib/materials/specificMaterials/terrain/redstoneTorch.glsl"

	overlayNoiseIntensity = 0.7, overlayNoiseEmission = 0.2;
} else if (color.r + color.b > color.g * 2.2 || color.r > 0.99) { // Amethyst Part
	#if GLOWING_AMETHYST >= 1
		#if defined GBUFFERS_TERRAIN && !defined IPBR_COMPAT_MODE
			vec2 absCoord = abs(signMidCoordPos);
			float maxBlockPos = max(absCoord.x, absCoord.y);
			emission = pow2(max0(1.0 - maxBlockPos) * color.g) * 5.4 + 1.2 * color.g;

			color.g *= 1.0 - emission * 0.07;
			color.rgb *= color.g;
		#else
			emission = pow2(color.g + color.b) * 0.32;
		#endif
	#endif

	#ifdef COATED_TEXTURES
		noiseFactor = 0.66;
	#endif
} else { // Tin Base
	#include "/lib/materials/specificMaterials/terrain/tinBlock.glsl"
}
