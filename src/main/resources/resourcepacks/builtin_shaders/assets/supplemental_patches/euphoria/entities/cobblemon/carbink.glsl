if (
	CheckForColor(color.rgb, vec3(244, 249, 255)) ||
	CheckForColor(color.rgb, vec3(214, 244, 255)) ||
	CheckForColor(color.rgb, vec3(172, 221, 254)) ||
	CheckForColor(color.rgb, vec3(133, 179, 234)) ||
	CheckForColor(color.rgb, vec3(97, 141, 201)) ||
	CheckForColor(color.rgb, vec3(86, 110, 117))
) {
	#include "/lib/materials/specificMaterials/terrain/diamondBlock.glsl"
} else if (color.r < 0.6 && color.g > 0.6 && color.b > 0.6) {
	#include "/lib/materials/specificMaterials/terrain/cobblestone.glsl"
}
