#if DOOM_AND_GLOOM_FOG == 1
    float lightDimming = FOG_LIGHT_DIMMING;
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    float lightDimming = mix(1.0, FOG_LIGHT_DIMMING, doomAndGloomFog);
#else
    float lightDimming = 1.0;
#endif