#ifdef WAVING_ROPE
    float waveMult = 1.0;

    #if COLORED_LIGHTING_INTERNAL > 0
        vec3 voxelPos = SceneToVoxel(playerPos);
        if (CheckInsideVoxelVolume(voxelPos)) {
            vec3 posBottom = SceneToVoxel(playerPos - vec3(0.0, 0.1, 0.0));
            vec3 posTop = SceneToVoxel(playerPos + vec3(0.0, 0.1, 0.0));

            int voxelBottom = int(texelFetch(voxel_sampler, ivec3(posBottom), 0).r);
            int voxelTop = int(texelFetch(voxel_sampler, ivec3(posTop), 0).r);

            if (mat % 4 == 1) {
                vec3 posNorth = SceneToVoxel(playerPos - vec3(0.0, 0.0, 0.1));
                vec3 posSouth = SceneToVoxel(playerPos + vec3(0.0, 0.0, 0.1));
                vec3 posEast = SceneToVoxel(playerPos - vec3(0.1, 0.0, 0.0));
                vec3 posWest = SceneToVoxel(playerPos + vec3(0.1, 0.0, 0.0));

                int voxelNorth = int(texelFetch(voxel_sampler, ivec3(posNorth), 0).r);
                int voxelSouth = int(texelFetch(voxel_sampler, ivec3(posSouth), 0).r);
                int voxelEast = int(texelFetch(voxel_sampler, ivec3(posEast), 0).r);
                int voxelWest = int(texelFetch(voxel_sampler, ivec3(posWest), 0).r);

                if (
                    (voxelBottom != voxelNumber && voxelBottom != 0) ||
                    (voxelTop != voxelNumber && voxelTop != 0) ||
                    (voxelNorth != voxelNumber && voxelNorth != 0) ||
                    (voxelSouth != voxelNumber && voxelSouth != 0) ||
                    (voxelEast != voxelNumber && voxelEast != 0) ||
                    (voxelWest != voxelNumber && voxelWest != 0)
                ) {
                    waveMult = 0.0;
                }
            } else {
                if (
                    (voxelBottom != voxelNumber && voxelBottom != 0) ||
                    (voxelTop != voxelNumber && voxelTop != 0)
                ) {
                    waveMult = 0.0;
                }
            }
        }
    #endif

    DoWave_Foliage(playerPos, worldPos, waveMult);
#endif