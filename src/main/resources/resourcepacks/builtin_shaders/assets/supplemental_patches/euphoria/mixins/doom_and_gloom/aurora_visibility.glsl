#if DOOM_AND_GLOOM_FOG == 1
    visibility *= FOG_AURORA_VISIBILITY;
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    visibility *= (1 - FOG_AURORA_VISIBILITY) * (1 - doomAndGloomFog) + FOG_AURORA_VISIBILITY * doomAndGloomFog;
#endif