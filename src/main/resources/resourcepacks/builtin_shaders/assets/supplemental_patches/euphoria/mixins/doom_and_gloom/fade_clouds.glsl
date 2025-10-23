#if DOOM_AND_GLOOM_FOG == 1
    clouds.rgb = vec3(0.5);
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    if (doomAndGloomFog > 0.0001) {
        clouds.rgb = vec3(0.5);
    }
#endif