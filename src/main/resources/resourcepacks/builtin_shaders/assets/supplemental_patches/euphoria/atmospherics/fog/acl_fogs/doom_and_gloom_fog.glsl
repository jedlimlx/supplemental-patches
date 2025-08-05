#ifdef MOD_DOOM_AND_GLOOM
    lightSample *= max(0.05, 1.0 - lTracePos * 0.03 * doomAndGloomFog);
    lightSample *= vec3(1.2, 0.7, 0.3);  // reddish light cuts through fog better
#endif