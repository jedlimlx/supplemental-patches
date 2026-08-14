#if (defined MOD_NETHEREXP && MC_VERSION >= 12100)
    color.rgb = mix(color.rgb, vec3(GetLuminance(color.rgb)), betrayedSmooth);
#endif
