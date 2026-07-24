void DoEctoplasmFog(inout vec4 color, float lViewPos) {
    #ifdef MOD_NETHEREXP
        float fog = 0.40 * lViewPos * inEctoplasm;
        fog = 1.0 - exp(-fog);

        color.rgb = mix(color.rgb, vec3(0.21, 0.89, 0.85), fog);
    #endif
}
