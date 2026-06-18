color = fireColor;
intensity = light.intensity;
#ifdef SOUL_SAND_VALLEY_OVERHAUL_INTERNAL
	color = mix(fireColor, halfSoulColor, inSoulValley);
#elif defined PURPLE_END_FIRE_INTERNAL
	color = halfEndBreathColor;
#endif
#if PHOTONICS_RESTIR_FLICKER_FIRE_INTERNAL > 0
	intensity *= flickerIntensity;
#endif
