#ifdef DARKER_END_SKY
    const vec3 originalEndSkyColor = vec3(E_SKY_COLORR, E_SKY_COLORG, E_SKY_COLORB) / 255.0 * E_SKY_COLORI * 0.50;
#else
    const vec3 originalEndSkyColor = vec3(E_SKY_COLORR, E_SKY_COLORG, E_SKY_COLORB) / 255.0 * E_SKY_COLORI;
#endif