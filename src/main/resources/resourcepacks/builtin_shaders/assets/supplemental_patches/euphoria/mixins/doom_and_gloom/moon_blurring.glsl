#if DOOM_AND_GLOOM_FOG == 1
    color.rgb = mix(color.rgb, vec3(0.5), pow2(acos(absVdotS) / acos(sunSizeFactor1)));
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    color.rgb = mix(color.rgb, vec3(0.5), doomAndGloomFog * pow2(acos(absVdotS) / acos(sunSizeFactor1)));
#endif