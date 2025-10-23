#if DOOM_AND_GLOOM_FOG == 1
    lightSample *= max(0.02, 1.0 - lTracePos * 0.03);
    lightSample *= 2.0;
    lightSample *= vec3(1.2, 0.7, 0.3);  // reddish light cuts through fog better
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    lightSample *= max(0.02, 1.0 - lTracePos * 0.03 * doomAndGloomFog);
    lightSample *= 1.0 + doomAndGloomFog;
    lightSample *= mix(vec3(1.0), vec3(1.2, 0.7, 0.3), doomAndGloomFog);  // reddish light cuts through fog better
#endif