vec3 hsvColor = rgb2hsv(color.rgb);
if (hsvColor.b < 0.6 && hsvColor.g < 0.1) {
	materialMask = OSIEBCA; // Intense Fresnel
	#include "/lib/materials/specificMaterials/terrain/anvil.glsl"
}
