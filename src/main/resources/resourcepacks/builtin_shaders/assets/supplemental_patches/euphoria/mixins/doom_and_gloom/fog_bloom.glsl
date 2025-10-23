#if DOOM_AND_GLOOM_FOG == 1
    bloomFogMult += FOG_BLOOM * eyeBrightnessM;
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    bloomFogMult += FOG_BLOOM * doomAndGloomFog * eyeBrightnessM;
#endif