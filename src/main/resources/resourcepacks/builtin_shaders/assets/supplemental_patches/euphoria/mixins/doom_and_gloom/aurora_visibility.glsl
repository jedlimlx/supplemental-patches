#ifdef DO_DOOM_AND_GLOOM_FOG
    visibility *= FOG_AURORA_VISIBILITY;
#elif MOD_DOOM_AND_GLOOM
    visibility *= (1 - FOG_AURORA_VISIBILITY) * (1 - doomAndGloomFog) + FOG_AURORA_VISIBILITY * doomAndGloomFog;
#endif