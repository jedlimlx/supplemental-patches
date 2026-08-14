#ifdef MOD_ELYSIUM_API
    vec3 sceneLighting = lightColorM * shadowLightMult + ambientColorM * max0(ambientMult + 15.0 * elysiumAmbientBrightness);
#else
    vec3 sceneLighting = lightColorM * shadowLightMult + ambientColorM * ambientMult;
#endif
