// adapted from lavaEdgeEffect.glsl
#if defined GBUFFERS_TERRAIN && !defined WORLD_CURVATURE
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

        float edge = mix(
            mix(isSurroundingFluid[0][0], isSurroundingFluid[0][1], fract(voxelPos.z + 0.5)),
            mix(isSurroundingFluid[1][0], isSurroundingFluid[1][1], fract(voxelPos.z + 0.5)),
            fract(voxelPos.x + 0.5)
        );

        edge = 1.0 - cos((edge * pi) / easeAmount); // ease in towards the centre of the block to create a better shape
        edge *= clamp01(blockUV.y - 0.3) * 10/7; // Gradient towards the bottom, so 0.3 is now 0

        vec3 absPlayerPos = abs(playerPos);
        float maxPlayerPos = max(absPlayerPos.x, max(absPlayerPos.y * 2.0, absPlayerPos.z));
        float edgeDecider = pow2(min1(maxPlayerPos / min(effectiveACLdistance, far) * 2.0)); // this is to make the effect fade at the edge of ACL range

        color.rgb = mix(color.rgb, edgeColor, edge * (1.0 - edgeDecider));
        emission = mix(emission, edgeEmission, edge * (1.0 - edgeDecider));
    }
#endif