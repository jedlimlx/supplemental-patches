#include "/lib/materials/specificMaterials/terrain/stone.glsl"

if (mat % 4 == 2) {
	lmCoordM.x *= 0.85;
	emission = color.b > 0.9 ? 2.5 : 0.0;
}

#ifdef COATED_TEXTURES
	noiseFactor = 0.66;
#endif
