#if FLESH_WAVING_ANYWHERE
    float waveMult = FLESH_WAVING_STRENGTH;
#else
    float waveMult = FLESH_WAVING_STRENGTH * inVisceralHeap;
#endif

#if COLORED_LIGHTING_INTERNAL > 0
vec3 voxelPos = SceneToVoxel(playerPos);
if (CheckInsideVoxelVolume(voxelPos)) {
    vec3 posBottom = SceneToVoxel(playerPos - vec3(0.0, 0.7, 0.0));
    vec3 posTop = SceneToVoxel(playerPos + vec3(0.0, 0.7, 0.0));
    vec3 posNorth = SceneToVoxel(playerPos - vec3(0.0, 0.0, 0.7));
    vec3 posSouth = SceneToVoxel(playerPos + vec3(0.0, 0.0, 0.7));
    vec3 posEast = SceneToVoxel(playerPos - vec3(0.7, 0.0, 0.0));
    vec3 posWest = SceneToVoxel(playerPos + vec3(0.7, 0.0, 0.0));

    int voxelBottom = int(texelFetch(voxel_sampler, ivec3(posBottom), 0).r);
    int voxelTop = int(texelFetch(voxel_sampler, ivec3(posTop), 0).r);
    int voxelNorth = int(texelFetch(voxel_sampler, ivec3(posNorth), 0).r);
    int voxelSouth = int(texelFetch(voxel_sampler, ivec3(posSouth), 0).r);
    int voxelEast = int(texelFetch(voxel_sampler, ivec3(posEast), 0).r);
    int voxelWest = int(texelFetch(voxel_sampler, ivec3(posWest), 0).r);

    vec3 fractPos = fract(worldPos);
    if (voxelEast != voxelNumber && voxelEast != 0)
    waveMult *= pow2(min1(fractPos.x / 0.5));

    if (voxelWest != voxelNumber && voxelWest != 0)
    waveMult *= pow2(min1((1 - fractPos.x) / 0.5));

    if (voxelBottom != voxelNumber && voxelBottom != 0)
    waveMult *= pow2(min1(fractPos.y / 0.5));

    if (voxelTop != voxelNumber && voxelTop != 0)
    waveMult *= pow2(min1((1 - fractPos.y) / 0.5));

    if (voxelNorth != voxelNumber && voxelNorth != 0)
    waveMult *= pow2(min1(fractPos.z / 0.5));

    if (voxelSouth != voxelNumber && voxelSouth != 0)
    waveMult *= pow2(min1((1 - fractPos.z) / 0.5));
}
#endif

DoWave_Flesh(playerPos, worldPos, waveMult);