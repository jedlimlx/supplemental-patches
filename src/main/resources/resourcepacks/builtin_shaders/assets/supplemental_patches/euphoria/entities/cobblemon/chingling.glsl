if (color.r > 0.9 && color.b < 0.7) {
	#include "/lib/materials/specificMaterials/terrain/goldBlock.glsl"
} else {
	smoothnessG = 0.25;
	highlightMult = 1.5;
	smoothnessD = 0.17;

	#ifdef COATED_TEXTURES
		noiseFactor = 0.33;
	#endif
}
