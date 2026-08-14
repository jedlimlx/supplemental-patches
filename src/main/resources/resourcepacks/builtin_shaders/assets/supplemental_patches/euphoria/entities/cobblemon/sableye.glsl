vec3 hsvColor = rgb2hsv(color.rgb);
if ((hsvColor.r > 0.52 && hsvColor.r < 0.56) || (color.r > 0.96 && color.g > 0.96 && color.b > 0.96)) {
	#include "/lib/materials/specificMaterials/terrain/diamondBlock.glsl"
}
