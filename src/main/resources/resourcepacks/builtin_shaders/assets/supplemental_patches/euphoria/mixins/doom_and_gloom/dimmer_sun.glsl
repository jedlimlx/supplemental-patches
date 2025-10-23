#if DOOM_AND_GLOOM_FOG == 1
    sunBrightness *= FOG_SUN_BRIGHTNESS;
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    sunBrightness *= mix(1, FOG_SUN_BRIGHTNESS, doomAndGloomFog);
#endif