subsurfaceMode = 1, noDirectionalShading = true, isFoliage = true;

#ifdef GLOWING_CHORUS_FLOWER
    if (color.r < color.g * 1.2 || color.r > 0.78) {
        emission = 1.0;
    }
#endif