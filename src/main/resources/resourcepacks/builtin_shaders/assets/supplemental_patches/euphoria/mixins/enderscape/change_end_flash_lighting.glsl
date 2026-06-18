#ifdef COLORED_ES_FLASH_LIGHTING
    vec3 endFlashColor = 1.5 * (enderscapeFlashColor / maxOf(enderscapeFlashColor)) * endFlashIntensityM * pow2(lightmapYM);
    ambientColorM *= mix(vec3(1.0), enderscapeFlashColor / maxOf(enderscapeFlashColor), endFlashIntensityM);
#else
    vec3 endFlashColor = (endOrangeCol + endLightColor) * endFlashIntensityM * pow2(lightmapYM);
#endif
