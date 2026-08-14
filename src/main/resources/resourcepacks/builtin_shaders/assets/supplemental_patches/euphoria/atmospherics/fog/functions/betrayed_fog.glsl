void DoBetrayedFog(inout vec4 color, float lViewPos) {
    #ifdef MOD_NETHEREXP
        float fog = 0.10 * lViewPos * betrayedSmooth;
        fog = 1.0 - exp(-fog);

        color.rgb = mix(color.rgb, vec3(0.7), fog);
    #endif
}
