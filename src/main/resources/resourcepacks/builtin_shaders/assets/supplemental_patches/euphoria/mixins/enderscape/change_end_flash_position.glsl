#if (ES_FLASH == 1 && defined MOD_ENDERSCAPE) || ES_FLASH == 2
	worldEndFlashPosition = normalize(worldEndFlashPosition);
#else
	worldEndFlashPosition = normalize(vec3(
		worldEndFlashPosition.x,
		0.15 * (END_BEAM_CENTER_ALT - playerPos.y),
		worldEndFlashPosition.z
	));
#endif
