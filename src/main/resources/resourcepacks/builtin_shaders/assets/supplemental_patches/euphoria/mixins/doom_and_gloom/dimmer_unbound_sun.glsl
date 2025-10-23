#if DOOM_AND_GLOOM_FOG == 1
    sunMoonMixer = pow(sunMoonMixer * 0.3, 0.2);
    sunBrightness *= FOG_UNBOUND_SUN_BRIGHTNESS;
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    sunMoonMixer = pow(sunMoonMixer * (1.0 - 0.7 * doomAndGloomFog), 0.2);
    sunBrightness *= mix(1.0, FOG_UNBOUND_SUN_BRIGHTNESS, doomAndGloomFog);
#endif