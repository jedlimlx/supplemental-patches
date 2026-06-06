#include "/lib/materials/specificMaterials/terrain/tinBlock.glsl"

float hue = rgb2hsv(color.rgb).r * 360;
if (color.r > 0.3 && color.b < 0.05 && color.g < 0.05) {  // Redstone Part
	#include "/lib/materials/specificMaterials/terrain/redstoneTorch.glsl"
} else if (hue < 60) {  // Tin Block
	#include "/lib/materials/specificMaterials/terrain/tinBlock.glsl"
}
