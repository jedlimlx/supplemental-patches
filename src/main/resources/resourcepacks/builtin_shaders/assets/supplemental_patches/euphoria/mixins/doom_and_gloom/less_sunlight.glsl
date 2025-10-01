#ifdef DO_DOOM_AND_GLOOM_FOG
    float lightDimming = FOG_LIGHT_DIMMING;
#elif MOD_DOOM_AND_GLOOM
    float lightDimming = mix(1.0, FOG_LIGHT_DIMMING, doomAndGloomFog);
#else
    float lightDimming = 1.0;
#endif