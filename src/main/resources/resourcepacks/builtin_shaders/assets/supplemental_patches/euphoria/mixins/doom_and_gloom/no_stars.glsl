#if DOOM_AND_GLOOM_FOG == 1
    return vec3(0.0);
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    if (doomAndGloomFog > 0.0001) return vec3(0.0);
#endif