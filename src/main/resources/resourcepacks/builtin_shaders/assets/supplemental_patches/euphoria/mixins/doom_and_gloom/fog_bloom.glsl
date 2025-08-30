#ifdef DO_DOOM_AND_GLOOM_FOG
    bloomFogMult += FOG_BLOOM * eyeBrightnessM;
#elif MOD_DOOM_AND_GLOOM
    bloomFogMult += FOG_BLOOM * doomAndGloomFog * eyeBrightnessM;
#endif