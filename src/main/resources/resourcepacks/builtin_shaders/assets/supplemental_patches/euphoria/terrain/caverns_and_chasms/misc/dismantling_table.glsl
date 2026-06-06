vec3 hsvColor = rgb2hsv(color.rgb) * vec3(360, 1, 1);
if ((hsvColor.r > 200 && hsvColor.r < 300) || hsvColor.r < 40) {  // Azalea Base
	#include "/lib/materials/specificMaterials/planks/blueAzaleaPlanks.glsl"
} else if (hsvColor.r > 310 && hsvColor.g > 0.3) {  // Spinel Studs
	#include "/lib/materials/specificMaterials/terrain/spinelBlock.glsl"
} else {  // Stone Top
	#include "/lib/materials/specificMaterials/terrain/stone.glsl"
}
