subsurfaceMode = 1, noDirectionalShading = true, isFoliage = true;

#ifdef GLOWING_CELESTIAL_GROWTHS
if (color.r > 0.8)
    emission = 0.9 * sqrt(color.r);
#endif

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif