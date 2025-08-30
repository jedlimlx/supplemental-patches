float waveMult = 0.75;
#if COLORED_LIGHTING_INTERNAL > 0
vec3 voxelPosP = SceneToVoxel(playerPos - vec3(0.0, 0.1, 0.0));

if (CheckInsideVoxelVolume(voxelPosP)) {
    int voxelP = int(texelFetch(voxel_sampler, ivec3(voxelPosP), 0).r);
    if (voxelP != 0) // not air
    waveMult = 0.0;
}
#endif
DoWave_Foliage(playerPos.xyz, worldPos, waveMult);