#include "/lib/materials/specificMaterials/terrain/copperBlock.glsl"
if (mat % 4 == 2 && color.r > 0.05 && color.g + color.b < 0.2) { // Redstone Parts
	emission = pow2(min(color.r, 0.9)) * 3.0;
	color.g = min(color.g, 0.1 * color.r);
	color.b = min(color.b, 0.1 * color.r);
}
