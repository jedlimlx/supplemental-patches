#if DOOM_AND_GLOOM_FOG == 1
    DoDoomAndGloomFog(color, lViewPos, fogOverride);
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    if (doomAndGloomFog > 0.0001) DoDoomAndGloomFog(color, lViewPos, fogOverride);
#endif