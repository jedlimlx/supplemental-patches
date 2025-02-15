#if WAVING_GILDED_BEADS >= 1
    float waveMult = 2.0;

    #if COLORED_LIGHTING_INTERNAL > 0
        vec3 voxelPos = SceneToVoxel(playerPos);
        if (CheckInsideVoxelVolume(voxelPos)) {
            vec3 coordsBeads = SceneToVoxel(playerPos + vec3(0.0, 0.1, 0.0));
            uint beadsVoxel = texelFetch(voxel_sampler, ivec3(coordsBeads), 0).r;
            if (beadsVoxel != uint(voxelNumber) && beadsVoxel != uint(0)) {
                waveMult = 0.0;
            }
        }
    #endif

    #if WAVING_GILDED_BEADS >= 2
        waveMult *= 2.0;
    #elif WAVING_GILDED_BEADS >= 3
        waveMult *= 5.0;
    #endif

    DoWave_Curtain(playerPos, worldPos, waveMult, (blockEntityId % 4) * 3.141592653 / 4.0);
#endif