vec4 effect = GetVolumetricSandstorm(color, translucentMult, nPlayerPos, playerPos, lViewPos, lViewPos1, dither) * rainFactor * hasSandstorm;

#if defined ATM_COLOR_MULTS || defined SPOOKY
    effect.rgb *= GetAtmColorMult();
#endif
#ifdef MOON_PHASE_INF_ATMOSPHERE
    effect.rgb *= moonPhaseInfluence;
#endif

color = mix(color, effect.rgb, effect.a);