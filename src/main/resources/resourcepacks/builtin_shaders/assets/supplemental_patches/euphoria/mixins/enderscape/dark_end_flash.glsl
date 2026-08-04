#if defined DARK_ES_END_FLASH && ((ES_FLASH == 1 && defined MOD_ENDERSCAPE) || ES_FLASH == 2)
    ambientColorM *= 1 - endFlashIntensityM;
    color.rgb *= mix(
        vec3(1.0), enderscapeFlashColor / maxOf(enderscapeFlashColor),
        0.6 * endFlashIntensityM * pow2(max0(lightmapYM - 0.25 * lightmapXM))
    );
#endif
