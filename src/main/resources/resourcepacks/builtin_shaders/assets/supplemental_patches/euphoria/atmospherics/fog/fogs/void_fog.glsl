float GetVoidFogFactor() {
    float factor = min(
        VOID_FOG_HIGHER_ALT - eyeAltitude,
        eyeAltitude - VOID_FOG_LOWER_ALT
    );
    return 1/(1 + exp(VOID_FOG_FADE_RATE*factor));
}

void DoVoidFog(inout vec4 color, float lViewPos) {
    #ifdef (DO_VOID_FOG == 1 && defined MOD_ENDERSCAPE) || DO_VOID_FOG == 2
        float fog = 0.25 * lViewPos * GetVoidFogFactor();
        fog = 1.0 - exp(-fog);

        color.rgb = mix(color.rgb, vec3(0.0), fog);
    #endif
}
