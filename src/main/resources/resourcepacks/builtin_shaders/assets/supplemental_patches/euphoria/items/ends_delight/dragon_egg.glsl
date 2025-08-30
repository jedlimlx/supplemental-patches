#ifdef EMISSIVE_DRAGON_EGG
    emission = color.b > 1.2 * color.r && color.b > 0.5 ? 1.6 * color.b : 0.0;
#endif