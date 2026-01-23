#if defined END && (defined ES_LIGHTMAP == 2 || (defined ES_LIGHTMAP == 1 && defined MOD_ENDERSCAPE))
    vec3 blockLighting = lightmapXM * blocklightCol + 0.4 * vec3(0.0, -oldLightmap.x * 0.1, oldLightmap.x * 0.16);
#else
    vec3 blockLighting = lightmapXM * blocklightCol;
#endif