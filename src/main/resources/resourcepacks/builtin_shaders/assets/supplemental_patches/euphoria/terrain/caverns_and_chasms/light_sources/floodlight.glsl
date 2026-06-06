#include "/lib/materials/specificMaterials/terrain/copperBlock.glsl"

#ifdef GBUFFERS_TERRAIN
vec2 pixelTexSize = ivec2(absMidCoordPos * 2.0 * atlasSize);
if (pixelTexSize.x == 15 && pixelTexSize.y == 15 && abs(signMidCoordPos.x) < 0.75 && abs(signMidCoordPos.y) < 0.75) {
	vec3 worldPos = playerPos.xyz + cameraPosition.xyz;
	vec3 blockPos = abs(fract(worldPos) - vec3(0.5));
	float maxBlockPos = max(blockPos.x, max(blockPos.y, blockPos.z));
	if (maxBlockPos > 0.4) {
		lmCoordM.x = max0(0.85 - absMidCoordPos.x - absMidCoordPos.y);
		emission = dot(color.rgb, color.rgb) * 0.8;
	}
}
#endif
