subsurfaceMode = 1, noDirectionalShading = true, isFoliage = true;

if (color.r > 0.8) {
    emission = 0.9 * sqrt(color.r);
}

#ifdef COATED_TEXTURES
    noiseFactor = 0.66;
#endif