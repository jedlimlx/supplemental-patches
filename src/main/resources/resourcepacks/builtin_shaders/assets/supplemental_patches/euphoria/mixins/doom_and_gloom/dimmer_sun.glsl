#ifdef DO_DOOM_AND_GLOOM_FOG
    sunBrightness *= FOG_SUN_BRIGHTNESS;
#elif MOD_DOOM_AND_GLOOM
    sunBrightness *= FOG_SUN_BRIGHTNESS * (1 - doomAndGloomFog) + FOG_SUN_BRIGHTNESS * doomAndGloomFog;
#endif