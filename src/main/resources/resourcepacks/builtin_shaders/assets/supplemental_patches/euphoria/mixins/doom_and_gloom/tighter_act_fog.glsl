#if DOOM_AND_GLOOM_FOG == 1
    light *= DG_ACT_FOG_SIZE;
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    light *= mix(1, DG_ACT_FOG_SIZE, doomAndGloomFog);
#endif