noSmoothLighting = true;
lmCoordM.x = min(lmCoordM.x, 0.9333);

vec3 worldPos = playerPos + cameraPosition;
vec3 fractPos = fract(worldPos.xyz);
vec2 coordM = abs(fractPos.xz - 0.5);

bool cauldronInteriorCheck = (max(coordM.x, coordM.y) < 0.375 && fractPos.y > 0.3);
#ifdef GBUFFERS_COLORWHEEL
	vec2 centered = abs(fract(texCoord));
	cauldronInteriorCheck = max(centered.x, centered.y) > 0.9;
#endif
if (cauldronInteriorCheck && NdotU > 0.9) {
	emission = 1.5 * pow2(color.r + color.b);
	sandNoiseIntensity = 0.0, mossNoiseIntensity = 0.0;
} else {
	#include "/lib/materials/specificMaterials/terrain/anvil.glsl"
}

#ifdef VOID_LACHRYMA_EDGE_EFFECT
	float easeAmount = 1.0;
	vec3 edgeColor = vec3(0.49, 0.12, 0.67) * 3.25;
	float edgeEmission = 0.8 + emission * 1.2;

	// adapted from lavaEdgeEffect.glsl
	#if (defined GBUFFERS_TERRAIN || defined GBUFFERS_WATER) && !defined WORLD_CURVATURE
	vec3 voxelPos = SceneToVoxel(playerPos);
	if (CheckInsideVoxelVolume(voxelPos)) {
		mat2 isSurroundingFluid = mat2(0, 0, 0, 0); // Thanks to gri for the help!

		ivec3 coordsFluid = ivec3(floor(vec3(voxelPos)));
		ivec3 coords = ivec3(floor(vec3(voxelPos.x - 0.5, voxelPos.y - 0.3, voxelPos.z - 0.5))); // shift coords to the center of the block
		uint fluidVoxel = texelFetch(voxel_sampler, ivec3(coordsFluid + ivec3(0, 1, 0)), 0).r; // coords for block above

		if (fluidVoxel != voxelNumber) {  // check if the above block is not the fluid, to only have the edge effect on the top most lava layer
			for (int i = 0; i < 2; i++) {  // check if the surrounding blocks are the fluid or not, 1 at the center of a non-fluid block, 0 at the center of a fluid block
				for (int j = 0; j < 2; j++) {
					uint voxel = texelFetch(voxel_sampler, ivec3(coords + ivec3(i, 0, j)), 0).r;
					isSurroundingFluid[i][j] = voxel != voxelNumber ? 1 : 0;
				}
			}
		}

		vec3 worldPos = round((cameraPosition + playerPos) * 16 - 0.5) / 16.0;
		float edge = mix(
			mix(isSurroundingFluid[0][0], isSurroundingFluid[0][1], fract(worldPos.z - 0.5)),
			mix(isSurroundingFluid[1][0], isSurroundingFluid[1][1], fract(worldPos.z - 0.5)),
			fract(worldPos.x - 0.5)
		);

		edge = 1.0 - cos((edge * pi) / easeAmount); // ease in towards the centre of the block to create a better shape
		edge *= mix(1.0, 0.0, Noise3D(vec3(0.1 * worldPos.xz, frameTimeCounter * 0.01)));
		edge *= clamp01(blockUV.y - 0.3) * 10/7; // Gradient towards the bottom, so 0.3 is now 0

		vec3 absPlayerPos = abs(playerPos);
		float maxPlayerPos = max(absPlayerPos.x, max(absPlayerPos.y * 2.0, absPlayerPos.z));
		float edgeDecider = pow2(min1(maxPlayerPos / min(effectiveACTdistance, far) * 2.0)); // this is to make the effect fade at the edge of ACT range

		color.a = mix(color.a, 1.0, edge * (1.0 - edgeDecider));
		color.rgb = mix(color.rgb, edgeColor, edge * (1.0 - edgeDecider));
		emission = mix(emission, edgeEmission, edge * (1.0 - edgeDecider));
	}
	#endif
#endif
