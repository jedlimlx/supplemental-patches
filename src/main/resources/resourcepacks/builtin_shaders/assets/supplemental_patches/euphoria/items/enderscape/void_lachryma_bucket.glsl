if (GetMaxColorDif(color.rgb) < 0.01) {
	#include "/lib/materials/specificMaterials/terrain/ironBlock.glsl"
} else {
	emission = 1.5 * pow2(color.r + color.b);
}
