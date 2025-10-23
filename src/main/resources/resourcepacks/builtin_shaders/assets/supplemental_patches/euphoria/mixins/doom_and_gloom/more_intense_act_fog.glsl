#if DOOM_AND_GLOOM_FOG == 1
    lightFogMult *= DG_ACT_FOG_INTENSITY;
#elif defined MOD_DOOM_AND_GLOOM && (DOOM_AND_GLOOM_FOG == 0)
    lightFogMult *= mix(1, DG_ACT_FOG_INTENSITY, doomAndGloomFog);
#endif