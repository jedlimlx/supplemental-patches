vec3 shadowMultFactor = GetShadow(shadowPos, lightmap.y, offset, shadowSamples, leaves, playerPos);
#if defined END && ((SOFTEN_END_SHADOWS == 1 && defined MOD_ENDERSCAPE) || SOFTEN_END_SHADOWS == 2)
	shadowMult *= mix(vec3(1.0), shadowMultFactor, SOFTEN_END_SHADOWS_I);
#else
	shadowMult *= shadowMultFactor;
#endif
