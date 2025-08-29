#ifdef DO_DOOM_AND_GLOOM_FOG
    return vec3(0.0);
#elif MOD_DOOM_AND_GLOOM
    if (doomAndGloomFog > 0.0001) return vec3(0.0);
#endif