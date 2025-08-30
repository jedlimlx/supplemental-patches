#ifdef EMISSIVE_DRAGON_EGG
    #ifdef GBUFFERS_TERRAIN
        if (color.b < 0.33 && color.r < 0.33 && color.g < 0.33)
            emission = float(color.b > 0.1) * 10.0 + 1.25;
    #else
        emission = color.b > 1.2 * color.r && color.b > 0.5 ? 1.6 * color.b : 0.0;
    #endif
#endif