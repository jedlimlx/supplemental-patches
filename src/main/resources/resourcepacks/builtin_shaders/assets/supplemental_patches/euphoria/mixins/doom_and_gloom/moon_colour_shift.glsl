#if DOOM_AND_GLOOM_FOG == 1
    moonColor *= mix(vec3(1.0), vec3(0.15, 0.2, 0.35), 0.3);
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    moonColor *= mix(vec3(1.0), vec3(0.15, 0.2, 0.35), 0.3 * doomAndGloomFog);
#endif