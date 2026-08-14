#ifdef (DO_VOID_FOG == 1 && defined MOD_ENDERSCAPE) || DO_VOID_FOG == 2
    color.rgb = mix(color.rgb, vec3(0.0), GetVoidFogFactor());
#endif
