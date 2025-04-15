void DoWave_TallFoliage(inout vec3 playerPos, vec3 worldPos, float _waveMult, int voxelNumber) {
    float waveMult = _waveMult;

    #if COLORED_LIGHTING_INTERNAL > 0
        vec3 voxelPos = SceneToVoxel(playerPos);
        if (CheckInsideVoxelVolume(voxelPos)) {
            vec3 posBottom = SceneToVoxel(playerPos - vec3(0.0, 0.1, 0.0));
            vec3 posTop = SceneToVoxel(playerPos + vec3(0.0, 0.1, 0.0));

            int voxelBottom = int(texelFetch(voxel_sampler, ivec3(posBottom), 0).r);
            int voxelTop = int(texelFetch(voxel_sampler, ivec3(posTop), 0).r);

            if (
                (voxelBottom != voxelNumber && voxelBottom != 0) ||
                (voxelTop != voxelNumber && voxelTop != 0)
            ) {
                waveMult = 0.0;
            }
        }
    #endif

    DoWave_Foliage(playerPos, worldPos, waveMult);
}