subsurfaceMode = 1, noDirectionalShading = true, isFoliage = true;

#if GLOWING_CELESTIAL_GROWTHS == 1
if (color.r > 0.8)
	emission = 0.2;
#elif GLOWING_CELESTIAL_GROWTHS == 2
if (color.r > 0.8)
    emission = 0.9 * sqrt(color.r);
#endif

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif
