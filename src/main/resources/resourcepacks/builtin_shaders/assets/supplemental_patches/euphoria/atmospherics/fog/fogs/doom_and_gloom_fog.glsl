#ifdef DO_DOOM_AND_GLOOM_FOG
    DoDoomAndGloomFog(color, lViewPos);
#elif MOD_DOOM_AND_GLOOM
    if (doomAndGloomFog > 0.0001) DoDoomAndGloomFog(color, lViewPos);
#endif